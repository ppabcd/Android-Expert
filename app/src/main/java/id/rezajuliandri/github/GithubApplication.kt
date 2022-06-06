package id.rezajuliandri.github

import android.app.Application
import id.rezajuliandri.core.di.databaseModule
import id.rezajuliandri.core.di.networkModule
import id.rezajuliandri.core.di.repositoryModule
import id.rezajuliandri.github.di.settingModule
import id.rezajuliandri.github.di.useCaseModule
import id.rezajuliandri.github.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
class GithubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@GithubApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    settingModule
                )
            )
        }

    }
}