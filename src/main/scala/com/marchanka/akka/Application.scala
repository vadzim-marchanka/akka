package com.marchanka.akka

import akka.actor.ActorSystem

object Application extends App {

  val system: ActorSystem = ActorSystem("companies")

  system.actorOf(SupervisorActor.props, "supervisor")

}
