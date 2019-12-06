package com.example.client.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.client.R
import com.example.client.R.id
import com.example.client.databinding.ActivityMainBinding
import com.example.client.ui.createProject.CreateProject
import com.example.client.ui.projectList.ProjectListActivity
import com.example.client.ui.taskList.TaskListActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

private val TAG:String = MainActivity::class.java.simpleName
class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory : MainViewModelFactory by instance()

    private lateinit var viewModel: MainViewModel
    private lateinit var projectListButton: Button
    private lateinit var createProjectButton: Button
    private lateinit var taskListButton: Button

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
        createProjectButton = findViewById(id.createProject)
        createProjectButton.setOnClickListener{
            val intent = Intent(this, CreateProject::class.java)
            Log.d("MainActivity","Change into CreateProjectActivity")
            startActivity(intent)
        }

        taskListButton = findViewById(R.id.taskList)
        taskListButton.setOnClickListener {

            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }


    }

}