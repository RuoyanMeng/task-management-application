package com.example.client.ui.taskList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.client.R
import com.example.client.ui.taskList.main.UploadImage
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_second, container, false)

        val fab2 = view.findViewById<FloatingActionButton>(R.id.fab2)


        fab2?.setOnClickListener { view ->

            val intent = Intent(activity, UploadImage::class.java)

            activity?.startActivity(intent)
        }

        return view

    }


}