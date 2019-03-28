package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.marchanka.akka.CompanyActor.CreateUser

object CompanyActor {
  def props(name:String): Props = Props(new CompanyActor(name))

  final case class CreateUser(name: String)
}

class CompanyActor(name: String) extends Actor with ActorLogging{

  override def preStart(): Unit = log.info("{} company actor started", name)
  override def postStop(): Unit = log.info("{} company actor stopped", name)

  override def receive: Receive = {
    case CreateUser(userName) =>
      log.info("Create user request is received with name: {}", userName)
      context.actorOf(UserActor.props(userName), userName)
  }

}
