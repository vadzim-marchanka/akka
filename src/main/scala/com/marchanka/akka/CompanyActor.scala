package com.marchanka.akka

import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor
import com.marchanka.akka.CompanyActor.{CreateUser, PersistentEvent, State, UserAte}

object CompanyActor {

  def props(name: String): Props = Props(new CompanyActor(name))

  final case class CreateUser(name: String)

  final case class UserAte(name: String)

  case class PersistentEvent(data: String)

  case class State(events: List[String] = Nil) {
    def updated(evt: PersistentEvent): State = copy(evt.data :: events)

    def size: Int = events.length

    override def toString: String = events.reverse.toString
  }

}

class CompanyActor(name: String) extends PersistentActor with ActorLogging {

  override def persistenceId: String = s"company-actor-$name"

  var state = State()

  def updateState(event: PersistentEvent): Unit =
    state = state.updated(event)

  def count = state.size

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

      persist(PersistentEvent(s"$count")) { event =>
        log.debug(s"Event persisted ${event.data}")

        updateState(event)
        context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.CompanyAteFromStart(name, count)
      }

    case _ => log.warning("Unknown message is received")
  }

  override def receiveRecover: Receive = {
    case event: PersistentEvent => updateState(event)
  }

}
