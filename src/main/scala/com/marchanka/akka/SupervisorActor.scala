package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.marchanka.akka.SupervisorActor.CreateCompany

class SupervisorActor extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("Supervisor actor started")

    context.actorOf(NewspaperActor.props, "newspaper")
    Thread.sleep(100)
  }

  override def postStop(): Unit = log.info("Supervisor actor stopped")

  override def receive: Receive = {
    case CreateCompany(name) =>
      log.info("CreateCompany message is received with name {}", name)
      context.actorOf(CompanyActor.props(name), name.toLowerCase)

    case _ => log.warning("Unknown message is received")
  }

}

object SupervisorActor {

  def props: Props = Props(new SupervisorActor)

  final case class CreateCompany(name: String)

}