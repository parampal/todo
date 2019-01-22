package com.parampal.todo.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parampal.todo.GetData
import com.parampal.todo.R
import com.parampal.todo.Todo

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TodoFragment.OnListFragmentInteractionListener] interface.
 */
class TodoFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var todos: ArrayList<Todo>? = null
    private var todoRecyclerViewAdapter: MyTodoRecyclerViewAdapter? = null
    private var todoRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        myCompositeDisposable = CompositeDisposable()
    }

    private fun loadData() {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(GetData::class.java)

        myCompositeDisposable?.add(requestInterface.getData()
            .observeOn(AndroidSchedulers.mainThread()) // send the Observable’s notifications to Android’s main UI thread
            .subscribeOn(Schedulers.io()) // perform api call on another thread
            .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(todos: List<Todo>) {
        this.todos = ArrayList(todos)
        Log.d("handleError", todos.toString())

        todoRecyclerViewAdapter = MyTodoRecyclerViewAdapter(this.todos!!, listener)
        todoRecyclerView?.adapter = todoRecyclerViewAdapter
    }

    private fun handleError(e: Throwable) {
        Log.d("handleError", e.message)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                todoRecyclerView = this
                loadData()
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        myCompositeDisposable?.clear()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Todo?)
    }

    companion object {

        const val BASE_URL = "https://jsonplaceholder.typicode.com"

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            TodoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
