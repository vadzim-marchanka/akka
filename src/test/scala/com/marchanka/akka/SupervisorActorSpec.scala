package com.marchanka.akka

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.marchanka.akka.CompanyActor.CreateUser
import com.marchanka.akka.SupervisorActor.CreateCompany
import com.marchanka.akka.UserActor.Eat
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

      Thread.sleep(2000)

      system.actorSelection("/user/supervisor/apple") ! CreateUser("user1")
      system.actorSelection("/user/supervisor/apple") ! CreateUser("user2")
      system.actorSelection("/user/supervisor/apple") ! CreateUser("user3")
      system.actorSelection("/user/supervisor/apple") ! CreateUser("user4")

      system.actorSelection("/user/supervisor/ibm") ! CreateUser("user5")
      system.actorSelection("/user/supervisor/ibm") ! CreateUser("user6")
      system.actorSelection("/user/supervisor/ibm") ! CreateUser("user7")
      system.actorSelection("/user/supervisor/ibm") ! CreateUser("user8")

      system.actorSelection("/user/supervisor/aeroflot") ! CreateUser("user9")
      system.actorSelection("/user/supervisor/aeroflot") ! CreateUser("user10")
      system.actorSelection("/user/supervisor/aeroflot") ! CreateUser("user11")
      system.actorSelection("/user/supervisor/aeroflot") ! CreateUser("user12")

      Thread.sleep(4000)

      system.actorSelection("/user/supervisor/apple/user4") ! Eat()
      system.actorSelection("/user/supervisor/apple/user4") ! Eat()
      system.actorSelection("/user/supervisor/apple/user1") ! Eat()
      system.actorSelection("/user/supervisor/apple/user1") ! Eat()
      system.actorSelection("/user/supervisor/apple/user2") ! Eat()
      system.actorSelection("/user/supervisor/apple/user2") ! Eat()
      system.actorSelection("/user/supervisor/ibm/user5") ! Eat()
      system.actorSelection("/user/supervisor/ibm/user6") ! Eat()
      system.actorSelection("/user/supervisor/ibm/user7") ! Eat()
      system.actorSelection("/user/supervisor/ibm/user8") ! Eat()
      system.actorSelection("/user/supervisor/aeroflot/user9") ! Eat()
      system.actorSelection("/user/supervisor/aeroflot/user10") ! Eat()
      system.actorSelection("/user/supervisor/aeroflot/user9") ! Eat()
      system.actorSelection("/user/supervisor/aeroflot/user10") ! Eat()

    }
  }

}
