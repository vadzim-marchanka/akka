package com.marchanka.akka

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.marchanka.akka.CompanyActor.{CreateUser, PersistentEvent, UserAte}

object CompanyActor {

  def props(name: String): Props = Props(new CompanyActor(name))

  final case class CreateUser(name: String)

  final case class UserAte(name: String)

  case class PersistentEvent(data: String)

}

class CompanyActor(name: String) extends PersistentActor with ActorLogging {

  override def persistenceId: String = s"company-actor-$name"

  var counter: Int = 0

  override def preStart(): Unit = {
    log.info("{} company actor started", name)

    ActorSelector.select(context, "/user/supervisor/newspaper") ! NewspaperActor.AnnounceCompanyCreated(name)
  }

  override def postStop(): Unit = log.info("{} company actor stopped", name)

  override def receiveCommand: Receive = {
    case CreateUser(userName) =>
      log.info("CreateUser message is received with name: {}", userName)

      context.actorOf(UserActor.props(userName), userName.toLowerCase)

    case UserAte(userName) =>
      log.info("CreateUser message is received with name: {}", userName)

      persist(PersistentEvent(s"$counter")) { event =>
        log.debug(s"Event persisted ${event.data}")

        counter = counter + 1
        context.system.eventStream.publish(event)
        if (lastSequenceNr % 10 == 0 && lastSequenceNr != 0) {
          saveSnapshot(counter)
          deleteMessages(lastSequenceNr)
        }

        context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.CompanyAteFromStart(name, counter)
      }
  }

  override def receiveRecover: Receive = {
    case event: PersistentEvent =>
      log.debug(s"Restoring state from event $event")

      counter = counter + 1

    case SnapshotOffer(_, snapshot: Int) =>
      log.debug(s"Restoring state from snapshot $snapshot")

      counter = snapshot
  }

}
