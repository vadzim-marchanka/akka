package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.marchanka.akka.CompanyActor.{CreateUser, UserAte}

object CompanyActor {

  def props(name: String): Props = Props(new CompanyActor(name))

  final case class CreateUser(name: String)

  final case class UserAte(name: String)

}

class CompanyActor(name: String) extends Actor with ActorLogging {

  var count = 0

  override def preStart(): Unit = {
    log.info("{} company actor started", name)

    context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.AnnounceCompanyCreated(name)
  }

  override def postStop(): Unit = log.info("{} company actor stopped", name)

  override def receive: Receive = {
    case CreateUser(userName) =>
      log.info("CreateUser message is received with name: {}", userName)
      context.actorOf(UserActor.props(userName), userName.toLowerCase)

    case UserAte(userName) =>
      log.info("CreateUser message is received with name: {}", userName)
      count = count + 1
      context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.CompanyAteFromStart(name, count)

    case _ => log.warning("Unknown message is received")
  }

}
