package com.example.get_apps.event_channel.events

class PackageNotification {
  private var PackageName: String
  private var Action: String

  constructor(PackageName: String, Action: String){
    this.PackageName = PackageName
    this.Action = Action
  }

  fun toJson(): Map<String, String>{
    return mapOf(
      "packageName" to PackageName,
      "action" to Action
    )
  }

}