package com.gp.base.screen.main

import androidx.lifecycle.*
import com.gp.base.network.model.ApiResponse
import com.gp.base.network.model.Project
import com.gp.base.network.repository.ProjectRepositoryInteractor
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val projectRepository: ProjectRepositoryInteractor) :
    ViewModel(),
    LifecycleObserver {

    val liveDataProjects = MutableLiveData<ApiResponse<List<Project>>>()

    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getProjectList()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.clear()
    }

    fun getProjectList(user : String = "JakeWharton") {
        disposable.add(projectRepository.getProjectList(user)
            .subscribe(
                { data ->
                    liveDataProjects.postValue(ApiResponse(data))
                }, { throwable ->
                    liveDataProjects.postValue(ApiResponse(throwable = throwable))
                }
            ))
    }
}
