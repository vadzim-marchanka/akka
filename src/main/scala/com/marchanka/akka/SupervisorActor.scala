package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}

object SupervisorActor {
  def props: Props = Props(new SupervisorActor)

  final case class CreateCompany(name: String)
}

class SupervisorActor extends Actor with ActorLogging{
  import SupervisorActor._

  override def preStart(): Unit = log.info("Supervisor actor started")
  override def postStop(): Unit = log.info("Supervisor actor stopped")

  override def receive: Receive = {
    case CreateCompany(name) =>
      log.info("Request is recieved with name = " + name)
      context.actorOf(CompanyActor.props(name), name)
  }

}
