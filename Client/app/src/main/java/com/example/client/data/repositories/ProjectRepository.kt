package com.example.client.data.repositories

import com.example.client.data.model.Project
import com.example.client.data.model.ProjectJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://130.233.101.91:8080/"

private val moshi = Moshi.Builder()
    .add(ProjectJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RepositoryService {

    @GET("projects/{kind}")
    fun getProjects(@Header("Authorization") authorization: String, @Path("kind") kind: String): Call<APIResponse<List<Project>>>

    @GET("project/{projectId}")
    fun getProject(@Path("projectId") id: String, @Header("Authorization") authorization: String): Call<APIResponse<Project>>
        
}

object ProjectRepository {

    val retrofitService : RepositoryService by lazy {
        retrofit.create(RepositoryService::class.java)
    }

}

class APIResponse<T> (
    val data: T?,
    val message: String?,
    val err: Map<String, String>?
)