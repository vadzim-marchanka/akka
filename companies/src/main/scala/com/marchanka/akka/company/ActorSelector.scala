package com.marchanka.akka.company

import akka.actor.{ActorRef, ActorRefFactory}

import scala.concurrent.Await
import scala.concurrent.duration._

object ActorSelector {

  def select(system: ActorRefFactory, path: String, period: Int = 100, times: Int = 10): ActorRef = {
    for (i <- 1 to times) {
      val future = system.actorSelection(path).resolveOne(period second)
      try {
        val actorRef = Await.result(future, period seconds)
        return actorRef
      } catch {
        case e: Exception => Thread.sleep(period)
      }
    }
    throw new Exception(s"Actor can not be selected $path")
  }

}
