package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}

object UserActor{
  def props(name:String) : Props = Props(new UserActor(name))
}

class UserActor(name:String) extends Actor with ActorLogging{

  override def preStart(): Unit = {
    log.info("{} user actor started", name)

    context.actorSelection("/user/supervisor/newspaper") ! NewspaperActor.AnnounceUserCreated(name)
  }
  override def postStop(): Unit = log.info("{} user actor stopped", name)

  override def receive: Receive = Actor.emptyBehavior

}
