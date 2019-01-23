package com.parampal.todo

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetData {
    @GET("/todos")
    fun getTodos(): Observable<List<Todo>>

    @GET("/todos")
    fun getTodosForUser(@Query("userId") userId: Int): Observable<List<Todo>>

    @GET("/users")
    fun getUsers(): Observable<List<User>>
}