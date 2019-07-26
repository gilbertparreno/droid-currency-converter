package com.gp.base.screen.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gp.base.network.model.ApiResponse
import com.gp.base.network.model.Project
import com.gp.base.network.repository.ProjectRepositoryInteractor
import com.gp.base.network.repository.ProjectRepositoryInteractorImpl
import com.gp.base.network.service.GithubService
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.net.SocketException


@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var githubService: GithubService

    lateinit var projectRepositoryInteractor: ProjectRepositoryInteractor

    lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        projectRepositoryInteractor = ProjectRepositoryInteractorImpl(githubService)
        this.mainViewModel = MainViewModel(this.projectRepositoryInteractor)

        RxJavaPlugins.setIoSchedulerHandler { scheduler -> Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline() }
    }

    @Test
    fun getProjectListTestSuccess() {
        // mock API response
        Mockito.`when`(this.githubService.getProjectList(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.just(ArgumentMatchers.anyList<Project>())
        }

        // attach fake observer
        val observer = mock(Observer::class.java) as Observer<ApiResponse<List<Project>>>
        this.mainViewModel.liveDataProjects.observeForever(observer)

        // invoke
        this.mainViewModel.getProjectList(ArgumentMatchers.anyString())

        // verify
        assertNotNull(this.mainViewModel.liveDataProjects.value)
        assertNotNull(this.mainViewModel.liveDataProjects.value?.data)
        assertNull(this.mainViewModel.liveDataProjects.value?.throwable)
    }

    @Test
    fun getProjectListTestError() {
        // mock API response
        Mockito.`when`(this.githubService.getProjectList(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.error<SocketException>(SocketException("Test error!"))
        }

        // attach fake observer
        val observer = mock(Observer::class.java) as Observer<ApiResponse<List<Project>>>
        this.mainViewModel.liveDataProjects.observeForever(observer)

        // invoke
        this.mainViewModel.getProjectList(ArgumentMatchers.anyString())

        // verify
        assertNotNull(this.mainViewModel.liveDataProjects.value)
        assertNull(this.mainViewModel.liveDataProjects.value?.data)
        assertNotNull(this.mainViewModel.liveDataProjects.value?.throwable)
    }
}