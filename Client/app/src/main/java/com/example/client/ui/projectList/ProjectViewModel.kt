package com.example.client.ui.projectList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.client.data.firebase.FirebaseSource
import com.example.client.data.model.Project
import com.example.client.data.repositories.APIResponse
import com.example.client.data.repositories.ProjectRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import javax.security.auth.callback.Callback

class ProjectViewModel(private val kind: String): ViewModel() {

    val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)

    private val projects: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>().also {
            loadProjects()
        }
    }

    fun getProjects(): LiveData<List<Project>> {
        return projects
    }

     fun loadProjects() {

        coroutineScope.launch {

            withContext(Dispatchers.Main) {

                val token: String =  FirebaseAuth.getInstance().getAccessToken(true).await().token!! //TODO method for that, and on fail redirect to login string

                ProjectRepository.retrofitService.getProjects(token, kind).enqueue(object: Callback, retrofit2.Callback<APIResponse<List<Project>>> {

                    override fun onFailure(call: Call<APIResponse<List<Project>>>, t: Throwable) {
                        Log.d("sarah", t.message)
                        projects.value = null
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<APIResponse<List<Project>>>, response: retrofit2.Response<APIResponse<List<Project>>>) {
                        Log.d("sarah", if (response.body() != null) "body is there" else "not" )

                        Log.d("sarah", response.body()!!.data!!.size.toString())
                        projects.value = response.body()!!.data
                    }
                })

            }
        }

    }

    /*private fun loadProjects2() {
        ProjectRepository.retrofitService.getProjects2().enqueue(object: Callback, retrofit2.Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("sarah", t.message)
                projects.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                Log.d("sarah", if (response.body() != null) "body is there" else "not" )
                Log.d("sarah", response.body()!!.toString())
            }
        })
    }*/


    /*private fun getProject(projectId: String) {
        ProjectRepository.retrofitService.getProject(projectId).enqueue(object: Callback, retrofit2.Callback<APIResponse<Project>> {

            override fun onFailure(call: Call<APIResponse<Project>>, t: Throwable) {
                Log.d("sarah", t.message)
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<APIResponse<Project>>, response: retrofit2.Response<APIResponse<Project>>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }*/

}

