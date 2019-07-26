package com.gp.base.network.repository

import com.gp.base.network.model.Project
import com.gp.base.network.service.GithubService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.Retrofit
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(private val githubService: GithubService) :
    ProjectRepositoryInteractor {

    override fun getProjectList(user: String): Single<List<Project>> {
        return githubService.getProjectList(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}