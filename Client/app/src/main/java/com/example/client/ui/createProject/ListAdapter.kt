package com.example.client.ui.createProject

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.startActivity
import com.example.client.R
import org.json.JSONObject

class ListAdapter (val context:Context,val list:ArrayList<User>):BaseAdapter(){

    var nameArray:ArrayList<String> = ArrayList()
    var IdArray:ArrayList<String> = ArrayList()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view:View = LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false)
       // val userID = view.findViewById(R.id.userlistID) as AppCompatTextView
        val userNAME: TextView = view.findViewById(R.id.userlistName)
        val addUser:Button=view.findViewById(R.id.addUser)
        userNAME.text = list[position].name.toString()

        addUser.setOnClickListener{

            nameArray.add(list[position].name)
            IdArray.add(list[position].id)
//            for(i:Int  in 0 until  nameArray.size){
//            Log.d("NameArray", "nameArray : " + nameArray.get(i))
//            }


        }

        return view

    }


    override fun getItem(position: Int): Any {


       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return list.size
    }

}