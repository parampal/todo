package com.parampal.todo

import io.reactivex.Observable
import retrofit2.http.GET

interface GetData {
    @GET("/todos")
    fun getTodos(): Observable<List<Todo>>

    @GET("/users/{user}/todos")
    fun getTodosForUser(): Observable<List<Todo>>

    @GET("/users")
    fun getUsers(): Observable<List<User>>
}