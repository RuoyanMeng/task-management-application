package com.example.client.ui.main

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.client.data.repositories.UserRepository
import com.example.client.utils.startLoginActivity


class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View){
        repository.logout()
        view.context.startLoginActivity()
    }
}