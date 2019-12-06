package com.example.client.data.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Project(val deadline: LocalDateTime?, /*val isFavorite: Map<String, Boolean>,*/ val administrator: String, val creationDate: LocalDateTime, val description: String, val id: String, val modificationDate: LocalDateTime?, val name: String, val isPersonal: Boolean/*, val contributors: List<String>?*/)

class ProjectJsonAdapter {

    @FromJson
    fun projectFromJson(map: Map<String, Any>): Project {

        val moshi = Moshi.Builder().build()
        val str = moshi.adapter<Map<*, *>>(MutableMap::class.java).toJson(map)

        val gson = Gson()
        val obj: JsonObject = gson.fromJson(str, JsonObject::class.java)

        //val obj = JSONObject(str)
        val isFavoriteType = object : TypeToken<HashMap<String, Boolean>>() {}.type
        val contributorsType= object : TypeToken<HashMap<String, Boolean>>() {}.type

        return Project(
            administrator = obj.getAsJsonPrimitive("administrator").asString,
            //isFavorite = gson.fromJson(obj.getAsJsonObject("isFavorite").toString(), isFavoriteType),
            creationDate = LocalDateTime.parse(obj.getAsJsonPrimitive("creationDate").asString,
                DateTimeFormatter.ISO_DATE_TIME),
            description = obj.getAsJsonPrimitive("description").asString,
            id = obj.getAsJsonPrimitive("id").asString,
            modificationDate = if (!obj.has("modificationDate")) null else LocalDateTime.parse(
                obj.getAsJsonPrimitive("modificationDate").asString,
                DateTimeFormatter.ISO_DATE_TIME),
            name = obj.getAsJsonPrimitive("name").asString,
            isPersonal = obj.getAsJsonPrimitive("isPersonal").asBoolean,
            deadline = if (!obj.has("deadline")) null else LocalDateTime.parse(
                obj.getAsJsonPrimitive("deadline").asString,
                DateTimeFormatter.ISO_DATE_TIME)
           // contributors = if (obj.getAsJsonPrimitive("isPersonal").asBoolean) null
           // else gson.fromJson(obj.getAsJsonArray("contributors").toString(), contributorsType)
        )

        //TODO DEAL WITH COLLABORATORS AND ISFAVORITE
    }

    @ToJson
    fun eventToJson(event: Project): Map<String, Any> {
        return HashMap<String, Any>()
    }
}
