package com.gp.base.screen.main

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.gp.base.R
import com.gp.base.app.App
import com.gp.base.network.model.ApiResponse
import com.gp.base.network.model.Project
import com.gp.base.network.service.GithubService
import com.gp.base.screen.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        let {
            DaggerMainComponent.builder()
                .appComponent((application as App).appComponent)
                .mainModule(MainModule())
                .build().inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.liveDataProjects.observe(this,
            Observer<ApiResponse<List<Project>>> { response -> Timber.d(response.data.toString()) })

        viewModel.getProjectList()
    }
}
