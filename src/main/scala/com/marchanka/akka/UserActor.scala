package com.marchanka.akka

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.marchanka.akka.CompanyActor.UserAte
import com.marchanka.akka.UserActor.{Eat, PersistentEvent}

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

    ActorSelector.select(context, "/user/supervisor/newspaper", 5, 1) ! NewspaperActor.AnnounceUserCreated(name)
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

        context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.UserAteFromStart(name, counter)
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
