package com.parampal.todo

import io.reactivex.Observable
import retrofit2.http.GET

interface GetData {
    @GET("/todos")
    fun getData(): Observable<List<Todo>>
}