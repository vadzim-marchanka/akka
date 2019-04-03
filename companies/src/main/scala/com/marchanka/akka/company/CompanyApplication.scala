package com.marchanka.akka.company

import akka.actor.ActorSystem
import com.marchanka.akka.company.CompanyActor.CreateUser
import com.marchanka.akka.company.SupervisorActor.CreateCompany
import com.marchanka.akka.company.UserActor.Eat
import com.typesafe.config.ConfigFactory

object CompanyApplication extends App {

  var config = ConfigFactory.load("company")

  val system: ActorSystem = ActorSystem("companies", config)

  val supervisor = system.actorOf(SupervisorActor.props, "supervisor")

  supervisor ! CreateCompany("Apple")
  supervisor ! CreateCompany("IBM")
  supervisor ! CreateCompany("Aeroflot")

  ActorSelector.select(system, "/user/supervisor/apple") ! CreateUser("user1")
  ActorSelector.select(system, "/user/supervisor/apple") ! CreateUser("user2")
  ActorSelector.select(system, "/user/supervisor/apple") ! CreateUser("user3")
  ActorSelector.select(system, "/user/supervisor/apple") ! CreateUser("user4")

  ActorSelector.select(system, "/user/supervisor/ibm") ! CreateUser("user5")
  ActorSelector.select(system, "/user/supervisor/ibm") ! CreateUser("user6")
  ActorSelector.select(system, "/user/supervisor/ibm") ! CreateUser("user7")
  ActorSelector.select(system, "/user/supervisor/ibm") ! CreateUser("user8")

  ActorSelector.select(system, "/user/supervisor/aeroflot") ! CreateUser("user9")
  ActorSelector.select(system, "/user/supervisor/aeroflot") ! CreateUser("user10")
  ActorSelector.select(system, "/user/supervisor/aeroflot") ! CreateUser("user11")
  ActorSelector.select(system, "/user/supervisor/aeroflot") ! CreateUser("user12")

  ActorSelector.select(system, "/user/supervisor/apple/user4") ! Eat()
  ActorSelector.select(system, "/user/supervisor/apple/user4") ! Eat()
  ActorSelector.select(system, "/user/supervisor/apple/user1") ! Eat()
  ActorSelector.select(system, "/user/supervisor/apple/user1") ! Eat()
  ActorSelector.select(system, "/user/supervisor/apple/user2") ! Eat()
  ActorSelector.select(system, "/user/supervisor/apple/user2") ! Eat()
  ActorSelector.select(system, "/user/supervisor/ibm/user5") ! Eat()
  ActorSelector.select(system, "/user/supervisor/ibm/user6") ! Eat()
  ActorSelector.select(system, "/user/supervisor/ibm/user7") ! Eat()
  ActorSelector.select(system, "/user/supervisor/ibm/user8") ! Eat()
  ActorSelector.select(system, "/user/supervisor/aeroflot/user9") ! Eat()
  ActorSelector.select(system, "/user/supervisor/aeroflot/user10") ! Eat()
  ActorSelector.select(system, "/user/supervisor/aeroflot/user9") ! Eat()
  ActorSelector.select(system, "/user/supervisor/aeroflot/user10") ! Eat()
  ActorSelector.select(system, "/user/supervisor/aeroflot/user10") ! Eat()

}
