package com.example.client.ui.projectList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R


class ProjectListFragment: Fragment(), ProjectRecyclerViewAdapter.OnListFragmentInteractionListener  {

   private lateinit var projectRecyclerViewAdapter: ProjectRecyclerViewAdapter
    private lateinit var projectRecyclerView: RecyclerView
    private lateinit var viewModel: ProjectViewModel
    private lateinit var viewModelFactory: ProjectViewModelFactory

    override fun onListFragmentInteraction(project: Project?) {
        Log.d("sarah", "List fragment interaction")
        TODO("not implemented")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activity?.let {
            viewModelFactory = ProjectViewModelFactory(it.application)
            viewModel = ViewModelProviders.of(it, viewModelFactory)[ProjectViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        return inflater.inflate(R.layout.fragment_project_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getProjects().observe(viewLifecycleOwner, Observer<List<Project>> { projects ->
            projectRecyclerViewAdapter = ProjectRecyclerViewAdapter(projects, this)
            projectRecyclerView = view.findViewById(R.id.list)
            projectRecyclerView.adapter = projectRecyclerViewAdapter
        })

    }

}


