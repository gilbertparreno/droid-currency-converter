package com.gp.base.screen.main

import com.gp.base.di.ActivityScope
import com.gp.base.network.repository.ProjectRepositoryInteractorImpl
import com.gp.base.network.repository.ProjectRepositoryInteractor
import com.gp.base.network.service.GithubService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun providesProjectRepository(githubService: GithubService): ProjectRepositoryInteractor {
        return ProjectRepositoryInteractorImpl(githubService)
    }

    @Provides
    @ActivityScope
    fun providesGithubService(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }
}