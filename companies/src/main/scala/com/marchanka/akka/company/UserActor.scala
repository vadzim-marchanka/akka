package com.marchanka.akka.company

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.marchanka.akka.api.NotificationApi.{AnnounceUserCreated, UserAteFromStart}
import com.marchanka.akka.company.CompanyActor.UserAte
import com.marchanka.akka.company.UserActor.{Eat, PersistentEvent}

object UserActor {

  def props(name: String): Props = Props(new UserActor(name))

  final case class Eat()

  case class PersistentEvent(data: String)

}

class UserActor(name: String) extends PersistentActor with ActorLogging {

  override def persistenceId: String = s"user-actor-$name"

  var counter = 0

  override def preStart(): Unit = {
    log.info("{} user actor started", name)

    ActorSelector.select(context, "akka://companies@127.0.0.1:8081/user/newspaper") ! AnnounceUserCreated(name)
  }

  override def postStop(): Unit = log.info("{} user actor stopped", name)

  override def receiveCommand: Receive = {
    case Eat() =>
      log.info("Eat message is received")

      persist(PersistentEvent(s"$counter")) { event =>
        log.debug(s"Event persisted ${event.data}")

        counter = counter + 1
        context.system.eventStream.publish(event)
        if (lastSequenceNr % 10 == 0 && lastSequenceNr != 0) {
          saveSnapshot(counter)
          deleteMessages(lastSequenceNr)
        }

        ActorSelector.select(context, "akka://companies@127.0.0.1:8081/user/newspaper") ! UserAteFromStart(name, counter)
        context.parent ! UserAte(name)
      }
  }

  override def receiveRecover: Receive = {
    case event: PersistentEvent =>
      log.debug(s"Restoring state from events $event")

      counter = counter + 1

    case SnapshotOffer(_, snapshot: Int) =>
      log.debug(s"Restoring state from snapshot $snapshot")

      counter = snapshot
  }
}
