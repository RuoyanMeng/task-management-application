package com.example.client.ui.projectList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.client.R
import com.example.client.data.model.Project


class ProjectRecyclerViewAdapter(private val mValues: List<Project>,
                                 private val mListener: OnListFragmentInteractionListener?)

    : RecyclerView.Adapter<ProjectFragment>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val project = v.tag as Project
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that a project has been selected.
            mListener?.onListFragmentInteraction(project)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectFragment {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_project, parent, false)
        return ProjectFragment(view)
    }

    override fun onBindViewHolder(holder: ProjectFragment, position: Int) {
        val project = mValues[position]
        holder.projectNameTextView.text = project.name
        holder.modificationDateTextView.text = if(project.modificationDate != null) project.modificationDate.toString() else "Never Modified"
        holder.deadlineTextView.text = if(project.deadline != null) project.deadline.toString() else "No deadline set"

        with(holder.mView) {
            tag = project
            setOnClickListener(mOnClickListener)
        }

    }

    override fun getItemCount(): Int = mValues.size

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Project?)
    }
}

