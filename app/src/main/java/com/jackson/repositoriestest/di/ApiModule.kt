package com.jackson.repositoriestest.di

import com.jackson.repositoriestest.http.GithubApiService
import org.koin.dsl.module.module
import retrofit2.Retrofit

/**
 * @author Leopold
 */
val apiModule = module {
    single(createOnStart = false) { get<Retrofit>().create(GithubApiService::class.java) }
}