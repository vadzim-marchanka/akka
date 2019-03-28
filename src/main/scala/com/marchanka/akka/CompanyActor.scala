package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.marchanka.akka.CompanyActor.CreateUser

object CompanyActor {
  def props(name:String): Props = Props(new CompanyActor(name))

  final case class CreateUser(name: String)
}

class CompanyActor(name: String) extends Actor with ActorLogging{

  override def preStart(): Unit = log.info(s"Company $name actor started")
  override def postStop(): Unit = log.info(s"Company $name actor stopped")

  override def receive: Receive = {
    case CreateUser(userName) =>
      context.actorOf(UserActor.props(userName), userName)
  }

}
