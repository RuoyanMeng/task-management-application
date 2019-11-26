package com.example.client.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.client.databinding.ActivityMainBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import android.widget.Button
import com.example.client.R
import com.example.client.ui.projectList.ProjectListActivity

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory : MainViewModelFactory by instance()

    private lateinit var viewModel: MainViewModel
    private lateinit var projectListButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        binding.viewmodel = viewModel

        projectListButton = findViewById(R.id.projectList)
        projectListButton.setOnClickListener {

        val intent = Intent(this, ProjectListActivity::class.java)
        startActivity(intent)
        }

    }

}