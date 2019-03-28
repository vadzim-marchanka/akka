package com.marchanka.akka

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.marchanka.akka.CompanyActor.CreateUser
import com.marchanka.akka.SupervisorActor.CreateCompany
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class SupervisorActorSpec(_system: ActorSystem) extends TestKit(_system)
  with Matchers
  with WordSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("SupervisorActorSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A Supervisor Actor" should {
    "creates company actors that allows to create user actors" in {
      val supervisor = system.actorOf(SupervisorActor.props)

      supervisor ! CreateCompany("Apple")
      supervisor ! CreateCompany("IBM")
      supervisor ! CreateCompany("Aeroflot")

      Thread.sleep(1000)

      system.actorSelection("/user/$a/Apple") ! CreateUser("user1")
      system.actorSelection("/user/$a/Apple") ! CreateUser("user2")
      system.actorSelection("/user/$a/Apple") ! CreateUser("user3")
      system.actorSelection("/user/$a/Apple") ! CreateUser("user4")

      system.actorSelection("/user/$a/IBM") ! CreateUser("user1")
      system.actorSelection("/user/$a/IBM") ! CreateUser("user2")
      system.actorSelection("/user/$a/IBM") ! CreateUser("user3")
      system.actorSelection("/user/$a/IBM") ! CreateUser("user4")

      system.actorSelection("/user/$a/Aeroflot") ! CreateUser("user1")
      system.actorSelection("/user/$a/Aeroflot") ! CreateUser("user2")
      system.actorSelection("/user/$a/Aeroflot") ! CreateUser("user3")
      system.actorSelection("/user/$a/Aeroflot") ! CreateUser("user4")


    }
  }

}
