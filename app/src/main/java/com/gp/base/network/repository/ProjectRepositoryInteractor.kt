package com.gp.base.network.repository

import androidx.lifecycle.LiveData
import com.gp.base.network.model.ApiResponse
import com.gp.base.network.model.Project
import io.reactivex.Single

interface ProjectRepositoryInteractor {
    fun getProjectList(user: String) : Single<List<Project>>

    interface Callback {
        fun onSubscribe()
        fun onError()
        fun onSuccess()
    }
}