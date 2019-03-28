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
      val supervisor = system.actorOf(SupervisorActor.props, "supervisor")

      supervisor ! CreateCompany("Apple")
      supervisor ! CreateCompany("IBM")
      supervisor ! CreateCompany("Aeroflot")

      Thread.sleep(1000)

      system.actorSelection("/user/supervisor/Apple") ! CreateUser("user1")
      system.actorSelection("/user/supervisor/Apple") ! CreateUser("user2")
      system.actorSelection("/user/supervisor/Apple") ! CreateUser("user3")
      system.actorSelection("/user/supervisor/Apple") ! CreateUser("user4")

      system.actorSelection("/user/supervisor/IBM") ! CreateUser("user5")
      system.actorSelection("/user/supervisor/IBM") ! CreateUser("user6")
      system.actorSelection("/user/supervisor/IBM") ! CreateUser("user7")
      system.actorSelection("/user/supervisor/IBM") ! CreateUser("user8")

      system.actorSelection("/user/supervisor/Aeroflot") ! CreateUser("user9")
      system.actorSelection("/user/supervisor/Aeroflot") ! CreateUser("user10")
      system.actorSelection("/user/supervisor/Aeroflot") ! CreateUser("user11")
      system.actorSelection("/user/supervisor/Aeroflot") ! CreateUser("user12")


    }
  }

}
