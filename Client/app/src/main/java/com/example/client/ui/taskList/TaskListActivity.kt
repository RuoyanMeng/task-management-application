package com.example.client.ui.taskList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.client.R
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.client.ui.main.MainActivity
import com.example.client.ui.taskList.main.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.FirebaseFirestore


lateinit var mDatabase: DatabaseReference
var toDoItemList: MutableList<ToDoItem>? = null
lateinit var adapter: ToDoItemAdapter
private var listViewItems: ListView? = null
val db = FirebaseFirestore.getInstance()

class TaskListActivity : AppCompatActivity(), ItemRowListener {

    object Constants {
        @JvmStatic val FIREBASE_ITEM: String = "todo_item"
    }

    companion object {
        private val TAG = "ClassName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

    //change the status
    override fun modifyItemState(itemObjectId: String, isDone: Boolean) {

        db.collection("tasks").document(itemObjectId).update("status", "completed")

    }

    //delete an item
    override fun onItemDelete(itemObjectId: String) {

        db.collection("tasks").document(itemObjectId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

        val intent = Intent(this, TaskListActivity::class.java)
        startActivity(intent)
    }

}