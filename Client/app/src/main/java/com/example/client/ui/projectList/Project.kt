package com.example.client.ui.projectList

import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Project(obj: JSONObject) {

    var isFavorite: Boolean = obj.getJSONObject("administrator").getBoolean("isFavorite")
    var userId: String = obj.getJSONObject("administrator").getString("user")
    var creationDate: LocalDateTime = LocalDateTime.parse(obj.getString("creationDate"), DateTimeFormatter.ISO_DATE_TIME)
    var description: String = obj.getString("description")
    var id: String = obj.getString("id")
    var modificationDate: LocalDateTime? = if(!obj.has("modificationDate")) null
                                            else LocalDateTime.parse(obj.getString("modificationDate"),DateTimeFormatter.ISO_DATE_TIME)
    var name: String = obj.getString("name")
    var isPersonal: Boolean = obj.getString("isPersonal") == "Personal"

}