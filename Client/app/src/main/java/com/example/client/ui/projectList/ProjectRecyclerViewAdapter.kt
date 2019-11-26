package com.example.client.ui.projectList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.client.R

import kotlinx.android.synthetic.main.fragment_project.view.*

class ProjectRecyclerViewAdapter(private val mValues: List<Project>,
                                 private val mListener: OnListFragmentInteractionListener?)

    : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ProjectFragment>() {

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
        holder.mIdView.text = project.id
        holder.mContentView.text = project.description

        with(holder.mView) {
            tag = project
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ProjectFragment(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Project?)
    }
}
