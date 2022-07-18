package id.rezajuliandri.github.di

import id.rezajuliandri.core.domain.usecase.GithubInteractor
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import id.rezajuliandri.github.detail.DetailViewModel
import id.rezajuliandri.github.followingfollowers.FollowingFollowersViewModel
import id.rezajuliandri.github.home.HomeViewModel
import id.rezajuliandri.github.repository.RepositoryViewModel
import id.rezajuliandri.github.setting.SettingPreferences
import id.rezajuliandri.github.setting.SettingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<GithubUseCase> { GithubInteractor(get()) }
}

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { RepositoryViewModel(get()) }
    viewModel { FollowingFollowersViewModel(get()) }
    viewModel { SettingViewModel(get()) }
}

val settingModule = module {
    factory { SettingPreferences(androidContext().applicationContext) }
}