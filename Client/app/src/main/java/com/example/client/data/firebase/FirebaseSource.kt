package com.example.client.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    /*fun login2(email: String, password: String) = Completable.create { emitter ->
        val payload = JSONObject()
        payload.put("email", email)
        payload.put("password", password)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = payload.toString().toRequestBody(mediaType)
        val okHttpClient = OkHttpClient()

        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://130.233.101.91:8080/login")
            .build()

        okHttpClient.newCall(request).execute().use { response ->

            if (response.isSuccessful) {
                val body = response.body!!.string()
                Log.d("sarah", body)

                val obj = JSONObject(body)

                if (!emitter.isDisposed) {
                    if (obj.has("token")) {
                        val token = obj.getString("token")
                        emitter.onComplete()

                    } else {
                        emitter.onError(Exception(obj.getString("err")))
                    }
                }

            } else {
                emitter.onError(Exception("response unsuccessful"))
            }

        }
    }*/


    fun login(email: String, password: String) = Completable.create { emitter ->

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful){
                    println("IT WORKS")
                    emitter.onComplete()

                }
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String, name: String) = Completable.create { emitter ->

        val payload = JSONObject()
        payload.put("email", email)
        payload.put("password", password)
        payload.put("name", name)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = payload.toString().toRequestBody(mediaType)
        val okHttpClient = OkHttpClient()

        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://mcc-fall-2019-g25.appspot.com/user")
            .build()


        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            println(response.body!!.string())
        }

    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser


}