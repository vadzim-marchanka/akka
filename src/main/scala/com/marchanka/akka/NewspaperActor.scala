package com.marchanka.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.marchanka.akka.NewspaperActor.{AnnounceCompanyCreated, AnnounceUserCreated, CompanyAteFromStart, UserAteFromStart}

object NewspaperActor {

  def props: Props = Props(new NewspaperActor)

  final case class AnnounceCompanyCreated(name: String)

  final case class AnnounceUserCreated(name: String)

  final case class UserAteFromStart(name: String, count: Int)

  final case class CompanyAteFromStart(name: String, count: Int)

}

class NewspaperActor extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("Newspaper actor started")

  override def postStop(): Unit = log.info("Newspaper actor stopped")

  override def receive: Receive = {
    case AnnounceCompanyCreated(name) =>
      log.info("AnnounceCompanyCreated message is received with name {}", name)
      println(s"New company $name was created!")

    case AnnounceUserCreated(name) =>
      log.info("AnnounceUserCreated message is received with name {}", name)
      println(s"New user $name was created!")

    case UserAteFromStart(name, count) =>
      log.info("UserAteFromStart message is received with name={}, count={}", name, count)
      println(s"$name has eaten $count meals")

    case CompanyAteFromStart(name, count) =>
      log.info("CompanyAteFromStart message is received with name={}, count={}", name, count)
      println(s"Company $name users have eaten $count meals so far.")

    case _ => log.warning("Unknown message is received")
  }

}
