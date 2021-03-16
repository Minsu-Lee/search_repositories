package com.jackson.repositoriestest.di

import com.jackson.repositoriestest.viewModel.GithubViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { GithubViewModel(get()) }
}