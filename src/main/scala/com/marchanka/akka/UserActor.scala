package com.marchanka.akka

import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor
import com.marchanka.akka.CompanyActor.UserAte
import com.marchanka.akka.UserActor.{Eat, PersistentEvent, State}

object UserActor {

  def props(name: String): Props = Props(new UserActor(name))

  final case class Eat()

  case class PersistentEvent(data: String)

  case class State(events: List[String] = Nil) {
    def updated(evt: PersistentEvent): State = copy(evt.data :: events)

    def size: Int = events.length

    override def toString: String = events.reverse.toString
  }

}

class UserActor(name: String) extends PersistentActor with ActorLogging {

  override def persistenceId: String = s"user-actor-$name"

  var state = State()

  def updateState(event: PersistentEvent): Unit =
    state = state.updated(event)

  def count = state.size

  override def preStart(): Unit = {
    log.info("{} user actor started", name)

    ActorSelector.select(context, "/user/supervisor/newspaper", 5, 1) ! NewspaperActor.AnnounceUserCreated(name)
  }

  override def postStop(): Unit = log.info("{} user actor stopped", name)

  override def receiveCommand: Receive = {
    case Eat() =>
      log.info("Eat message is received")

      persist(PersistentEvent(s"$count")) { event =>
        log.debug(s"Event persisted ${event.data}")

        updateState(event)
        context.system.eventStream.publish(event)
        context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.UserAteFromStart(name, count)
        context.parent ! UserAte(name)
      }

    case _ => log.warning("Unknown message is received")
  }

  override def receiveRecover: Receive = {
    case event: PersistentEvent => updateState(event)
  }
}
