package com.marchanka.akka.newspaper

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object NewspaperApplication extends App {

  var config = ConfigFactory.load("newspaper")

  val system: ActorSystem = ActorSystem("companies", config)

  val supervisor = system.actorOf(NewspaperActor.props, "newspaper")

  system.actorOf(Props[SimpleClusterListener], name = "clusterListener")

}
