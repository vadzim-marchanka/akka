package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.marchanka.akka.CompanyActor.UserAte
import com.marchanka.akka.UserActor.Eat

object UserActor {

  def props(name: String): Props = Props(new UserActor(name))

  final case class Eat()

}

class UserActor(name: String) extends Actor with ActorLogging {

  var count = 0

  override def preStart(): Unit = {
    log.info("{} user actor started", name)

    ActorSelector.select(context, "/user/supervisor/newspaper", 5, 1) ! NewspaperActor.AnnounceUserCreated(name)
  }

  override def postStop(): Unit = log.info("{} user actor stopped", name)

  override def receive: Receive = {
    case Eat() =>
      log.info("Eat message is received")
      count = count + 1
      context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.UserAteFromStart(name, count)
      context.parent ! UserAte(name)

    case _ => log.warning("Unknown message is received")
  }

}
