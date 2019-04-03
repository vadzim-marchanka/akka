package com.marchanka.akka.api

object NotificationApi {

  final case class AnnounceCompanyCreated(name: String)

  final case class AnnounceUserCreated(name: String)

  final case class UserAteFromStart(name: String, count: Int)

  final case class CompanyAteFromStart(name: String, count: Int)

}
