package com.gp.base.network.service

import com.gp.base.network.model.Project
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GithubService {

    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String): Single<List<Project>>
}