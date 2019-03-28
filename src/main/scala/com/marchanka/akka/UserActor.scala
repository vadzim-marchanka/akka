package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.marchanka.akka.CompanyActor.UserAte
import com.marchanka.akka.UserActor.Eat

object UserActor {

  def props(name: String, companyRef: ActorRef): Props = Props(new UserActor(name, companyRef))

  final case class Eat()

}

class UserActor(name: String, companyRef: ActorRef) extends Actor with ActorLogging {

  var count = 0

  override def preStart(): Unit = {
    log.info("{} user actor started", name)

    context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.AnnounceUserCreated(name)
  }

  override def postStop(): Unit = log.info("{} user actor stopped", name)

  override def receive: Receive = {
    case Eat() =>
      log.info("Eat message is received")
      count = count + 1
      context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.UserAteFromStart(name, count)
      companyRef ! UserAte(name)
  }

}
