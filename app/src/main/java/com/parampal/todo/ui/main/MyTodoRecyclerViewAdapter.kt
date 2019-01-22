package com.parampal.todo.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.parampal.todo.R
import com.parampal.todo.Todo


import com.parampal.todo.ui.main.TodoFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_todo.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTodoRecyclerViewAdapter(
    private val mValues: List<Todo>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyTodoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Todo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitleView.text = item.title
        holder.mCompletedView.text = item.completed.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.title
        val mCompletedView: TextView = mView.completed

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
