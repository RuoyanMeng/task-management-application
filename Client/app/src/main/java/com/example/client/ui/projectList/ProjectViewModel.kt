package com.example.client.ui.projectList

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.client.data.repositories.MyRequestQueue
import org.json.JSONArray

class ProjectViewModel(private val application: Application): ViewModel() {

    private val projects: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>().also {
            loadProjects()
        }
    }

    fun getProjects(): LiveData<List<Project>> {
        return projects
    }

    private fun loadProjects() {
        val queue = MyRequestQueue.getInstance(application.applicationContext)
        val url = "https://my-json-server.typicode.com/ssssarah/testThing/projects"

        val stringRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                projects.setValue(List(response.length()) { i -> Project(response.getJSONObject(i)) })
            },
            Response.ErrorListener { /* TODO */ })

        queue.addToRequestQueue(stringRequest)

    }

}