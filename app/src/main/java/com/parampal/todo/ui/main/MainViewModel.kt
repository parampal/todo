package com.parampal.todo.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.util.Log
import com.parampal.todo.GetData
import com.parampal.todo.Todo
import com.parampal.todo.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    public var todos = MutableLiveData <ArrayList<Todo>>()
    public var users = MutableLiveData <ArrayList<User>>()
    private var myCompositeDisposable: CompositeDisposable? = null
    private val BASE_URL = "https://jsonplaceholder.typicode.com"

    init {
        myCompositeDisposable = CompositeDisposable()
    }

    public fun getUserByPosition(pos: Int): User {
        return this.users.value!!.get(pos)
    }

    public fun loadTodosForPosition(pos: Int) {
        if (pos < 0) {
            return loadTodos()
        }
        Log.d("loadtodos", pos.toString())
        Log.d("loadtodos", getUserByPosition(pos).toString())

        loadTodosForUser(getUserByPosition(pos))
    }

    public fun getUserList(): MutableList<String> {
        val usersListOfStrings: MutableList<String> = mutableListOf("All Users")
        this.users.value!!.forEach { user: User ->
            usersListOfStrings.add(user.name)
        }
        return usersListOfStrings
    }

    public fun loadUsers() {
        myCompositeDisposable?.add(getRequest().getUsers()
            .observeOn(AndroidSchedulers.mainThread()) // send the Observable’s notifications to Android’s main UI thread
            .subscribeOn(Schedulers.io()) // perform api call on another thread
            .subscribe(this::handleUsersResponse, this::handleError))
    }

    public fun loadTodos() {
        myCompositeDisposable?.add(getRequest().getTodos()
            .observeOn(AndroidSchedulers.mainThread()) // send the Observable’s notifications to Android’s main UI thread
            .subscribeOn(Schedulers.io()) // perform api call on another thread
            .subscribe(this::handleTodoResponse, this::handleError))
    }

    public fun loadTodosForUser(user: User) {
        myCompositeDisposable?.add(getRequest().getTodosForUser(user.id)
            .observeOn(AndroidSchedulers.mainThread()) // send the Observable’s notifications to Android’s main UI thread
            .subscribeOn(Schedulers.io()) // perform api call on another thread
            .subscribe(this::handleTodoResponse, this::handleError))
    }

    private fun getRequest() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(GetData::class.java)

    private fun handleUsersResponse(users: List<User>) {
        this.users.postValue(ArrayList(users))
        Log.d("Users", users.toString())
    }

    private fun handleTodoResponse(todos: List<Todo>) {
        this.todos.postValue(ArrayList(todos))
        Log.d("handleError", todos.toString())
    }

    private fun handleError(e: Throwable) {
        Log.d("handleError", e.message)
    }

    override fun onCleared() {
        myCompositeDisposable?.clear()
    }
}