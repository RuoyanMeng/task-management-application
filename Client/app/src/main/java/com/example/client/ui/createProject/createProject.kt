package com.example.client.ui.createProject
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.example.client.data.firebase.FirebaseSource

import com.example.client.data.repositories.UserRepository

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.example.client.R
import com.example.client.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_login.view.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.FileDescriptor
import java.io.IOException
class administrator{
    var isFavorite:Boolean ? = null
    var newUser:String?=null


    constructor(isFavorite:Boolean,newUser: String){
        this.isFavorite = isFavorite
        this.newUser = newUser
    }
    constructor()
}
class CreateProject : AppCompatActivity() {
    lateinit var editNewProjectName:EditText
    lateinit var editNewProjectDescription:EditText
    lateinit var buttonAssignment:Button
    lateinit var buttonFinal:Button
    lateinit var name:String
    lateinit var description: String
    var P:Int = 0
    var cID:String =""

    companion object{
        const val EXTRA_CollaboratorID = "EXTRA_CollaboratorID"
        const val EXTRA_CollaboratorName = "EXTRA_CollaboratorName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)


        editNewProjectName = findViewById(R.id.editNewProjectName)
        editNewProjectDescription = findViewById(R.id.editNewProjectDescription)
       // editNewProjectKeywords = findViewById(R.id.editNewProjectKeywords)



        buttonAssignment = findViewById(R.id.buttonAssignment)

        buttonFinal = findViewById(R.id.buttonFinalProject)
        val coID = intent.getStringArrayListExtra(EXTRA_CollaboratorID)
        Log.d("createProjectActivity","coID is :"+ coID)

        val coName = intent.getStringArrayListExtra(EXTRA_CollaboratorName)
        Log.d("createProjectActivity","coID is :"+ coName)

        buttonAssignment.setOnClickListener{
            Log.d("createProjectActivity","Try assignment")
            withItems(it)
        }
        buttonFinal.setOnClickListener{
            Log.d("createProjectActivity","Try final create")
            Log.d("P","P: " + P)
            if(P==0) {
                createNewProject(coID, coName)
            }
            else{
                personalCreate()
            }
            val intent_back = Intent(this, MainActivity::class.java)
            Log.d("CreateActivity","Change into ListUserActivity")
            startActivity(intent_back)
            Log.d("createProjectActivity","Create Successfully")

        }


    }
    private fun personalCreate(){
        name = editNewProjectName.text.toString()
        editNewProjectName.visibility = View.VISIBLE
        description = editNewProjectDescription.text.toString()
        editNewProjectDescription.visibility = View.VISIBLE

        var currentUser:String?= FirebaseAuth.getInstance().currentUser?.uid

        val parameter1 =JSONObject()
        parameter1.put("isFavorite",true)
        parameter1.put("user",currentUser)
        Log.d("createProjectActivity","parameter:"+parameter1)
        val parameter2 = JSONObject()
        parameter2.put("isFavorite",true)
        val parameter3 = JSONObject()

        parameter3.put(currentUser.toString(),parameter2)

        Log.d("Parameter3 is:","IT is:" + parameter3)

        val newProject = JSONObject()
        newProject.put("description",description)
        newProject.put("administrator",parameter1)
        Log.d("createProjectActivity","newProject is"+newProject)

        newProject.put("isPersonal",true)

        newProject.put("name",name)

        newProject.put("isFavorite",parameter3)
        newProject.put("collaborators",null)

        Log.d("createProjectActivity","newProject is"+newProject)



        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = newProject.toString().toRequestBody(mediaType)

        val okHttpClient1 = OkHttpClient()
        val TextViewResult:TextView = findViewById(R.id.resultView)
        val request1 = Request.Builder()
            .method("POST", requestBody)
            .url("http://mcc-fall-2019-g25.appspot.com/project")
            .build()
        Log.d("createProjectActivity","Request build successfully")
        var call=okHttpClient1.newCall(request1)
        call.enqueue(object:Callback{

            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseHTTP = response.body.toString()
                    Log.d("GET Back from HTTP", "RESPONSE IS :" + responseHTTP)
                    Thread(Runnable {
                        this@CreateProject.runOnUiThread({
                            TextViewResult.text = responseHTTP
                            Log.d("GET Back from HTTP", "Run on UI Thread now")
                        })

                    })
                }
            }
        })



        Toast.makeText(this,"Create a Pernal Project Successfully",Toast.LENGTH_LONG).show()


    }

    private fun withItems(view: View) {

        val items = arrayOf("Personal", "Group", "Cancel")
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle("Select Options")
            setItems(items) { dialog, which ->
                val selected = items[which]
                if(selected=="Personal"){
                    Log.d("Selection","Personal")
                    P = 1
                }
                    else if(selected=="Group") {
                    P = 0

                    Log.d("p", "P:" + P)
                    Log.d("Selection", "Group")
                    selectCollabrator()

                }



                // Toast.makeText(applicationContext, items[which] + " is clicked", Toast.LENGTH_SHORT).show()
            }
            // items[0].setOnClickListener()


//            setPositiveButton("OK", positiveButtonClick)
            show()
        }
    }
    private fun selectCollabrator(){
        val intent = Intent(this, UserList::class.java)
        Log.d("CreateActivity","Change into ListUserActivity")
        startActivity(intent)
    }
    private fun createNewProject(coID:ArrayList<String>,coName:ArrayList<String>){
        var cID= coID
        Log.d("createProjectActivity","cID is :"+ "lalal")
        var cName= coName
        name = editNewProjectName.text.toString()
        editNewProjectName.visibility = View.VISIBLE
        description = editNewProjectDescription.text.toString()
        editNewProjectDescription.visibility = View.VISIBLE

        var currentUser:String?= FirebaseAuth.getInstance().currentUser?.uid

        Log.d("createProjectActivity","Name is :"+ name)
        Log.d("createProjectActivity","Description is:"+description)
        Log.d("createProjectActivity","CurrentUser is "+currentUser)

        val parameter1 =JSONObject()
        parameter1.put("isFavorite",true)
        parameter1.put("user",currentUser)
        Log.d("createProjectActivity","parameter:"+parameter1)
        val parameter2 = JSONObject()
        parameter2.put("isFavorite",true)
        val parameter3 = JSONObject()

        for(i in 0 until cID.size){
            var innerCID:String = cID.get(i)
            parameter3.put(innerCID,parameter2)
        }



        Log.d("Parameter3 is:","IT is:" + parameter3)

        val newProject = JSONObject()
        newProject.put("description",description)
        newProject.put("administrator",parameter1)
        Log.d("createProjectActivity","newProject is"+newProject)

        newProject.put("isPersonal",false)

        newProject.put("name",name)

        newProject.put("isFavorite",parameter3)
        newProject.put("collaborators",cID)
        Log.d("createProjectActivity","newProject is"+newProject)



        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = newProject.toString().toRequestBody(mediaType)

        val okHttpClient1 = OkHttpClient()
        val TextViewResult:TextView = findViewById(R.id.resultView)
        val request1 = Request.Builder()
            .method("POST", requestBody)
            .url("http://mcc-fall-2019-g25.appspot.com/project")
            .build()
        Log.d("createProjectActivity","Request build successfully")
        var call=okHttpClient1.newCall(request1)
        call.enqueue(object:Callback{

            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseHTTP = response.body.toString()
                    Log.d("GET Back from HTTP", "RESPONSE IS :" + responseHTTP)
                    Thread(Runnable {
                        this@CreateProject.runOnUiThread({
                            TextViewResult.text = responseHTTP
                            Log.d("GET Back from HTTP", "Run on UI Thread now")
                        })

                    })
                }
            }
        })



        Toast.makeText(this,"Create a Group Project Successfully",Toast.LENGTH_LONG).show()



    }



}
