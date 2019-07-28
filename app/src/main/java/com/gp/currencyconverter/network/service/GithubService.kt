package com.gp.currencyconverter.network.service

import com.gp.currencyconverter.network.model.Project
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GithubService {

    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String): Single<List<Project>>
}