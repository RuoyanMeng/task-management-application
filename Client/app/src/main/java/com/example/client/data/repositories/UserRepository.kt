package com.example.client.data.repositories

import com.example.client.data.firebase.FirebaseSource


class UserRepository (
    private val firebase: FirebaseSource
){
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()
}