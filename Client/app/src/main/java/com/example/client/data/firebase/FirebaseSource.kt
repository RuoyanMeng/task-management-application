package com.example.client.data.firebase

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


    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String, name:String) = Completable.create { emitter ->

        val payload = JSONObject()
        payload.put("email",email)
        payload.put("password",password)
        payload.put("name",name)

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


//        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//            if (!emitter.isDisposed) {
//                if (it.isSuccessful)
//                    emitter.onComplete()
//                else
//                    emitter.onError(it.exception!!)
//            }
//        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser



}