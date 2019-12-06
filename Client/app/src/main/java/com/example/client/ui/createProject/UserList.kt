package com.example.client.ui.createProject

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.example.client.R
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class UserList : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        //  val url = "http://mcc-fall-2019-g25.appspot.com/user/Bfuvurt7gchls1xyq8YbYW2hf442"


        val editUserName = findViewById<EditText>(R.id.userFindbyName)

        var username: String = editUserName.toString()
        editUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                username = "http://mcc-fall-2019-g25.appspot.com/users/" + s.toString()

                Log.d("Collabrator Assignment:", "userSearchUrl is : " + username)

            }

        })


        val userFindbyName_button: Button = findViewById(R.id.findByName)
        userFindbyName_button.setOnClickListener {

            AsyncTaskHandleJson().execute(username)

        }
    }

    inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }

            } finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            handleJson(result)
        }


    }

    private fun handleJson(responseData: String?) {

        var addusers: JSONObject

        val jsonArray = JSONArray(responseData)
        val list = ArrayList<User>()
        var x = 0
        while (x < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(x)
            list.add(
                User(
                    jsonObject.getString("uid"),
                    jsonObject.getString("name")
                )
            )

            x++
        }
        val adapter = ListAdapter(this, list)
        var listView: ListView = findViewById(R.id.userlistView)
        listView.adapter = adapter

        val add_button: Button = findViewById(R.id.add_button)
        add_button.setOnClickListener {

            for (item in adapter.nameArray) {
                var ccName: String = item
                Log.d("ccName", "ccName is :" + ccName)
            }
            Log.d("cc","NameArray is: " + adapter.nameArray)
            val sendCo = Intent(this, CreateProject::class.java)
            sendCo.putExtra(CreateProject.EXTRA_CollaboratorID, adapter.IdArray)
            sendCo.putExtra(CreateProject.EXTRA_CollaboratorName, adapter.nameArray)
            startActivity(sendCo)

//        val add_button:Button = findViewById(R.id.add_button)
//        add_button.setOnClickListener {
//
//            val sendCo = Intent(this, CreateProject::class.java)
//            sendCo.putExtra(CreateProject.EXTRA_CollaboratorID, collaboratorID)
//            sendCo.putExtra(CreateProject.EXTRA_CollaboratorName, collaboratorName)
//
//            startActivity(sendCo)
//        }

//                val addUser_button:Button = findViewById(R.id.addUser)
//                addUser_button.setOnClickListener{
//
//                }


            Log.d("SelectActivity", "Select successfully")


        }


    }
}



