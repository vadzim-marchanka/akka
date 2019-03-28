package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}

object UserActor{
  def props(name:String) : Props = Props(new UserActor(name))
}

class UserActor(name:String) extends Actor with ActorLogging{

  override def preStart(): Unit = log.info(s"UserActor $name actor started")
  override def postStop(): Unit = log.info(s"UserActor $name actor stopped")

  override def receive: Receive = Actor.emptyBehavior

}
