package id.rezajuliandri.core.di

import androidx.room.Room
import id.rezajuliandri.core.BuildConfig
import id.rezajuliandri.core.data.GithubRepository
import id.rezajuliandri.core.data.local.LocalDataSource
import id.rezajuliandri.core.data.local.room.GithubDatabase
import id.rezajuliandri.core.data.remote.RemoteDataSource
import id.rezajuliandri.core.data.remote.network.ApiService
import id.rezajuliandri.core.domain.repository.IGithubRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory {
        get<GithubDatabase>().githubDao()
    }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("github".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            GithubDatabase::class.java, "Github.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}
val networkModule = module {
    single {
        val hostname = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/ORtIOYkm5k6Nf2tgAK/uwftKfNhJB3QS0Hs608SiRmE=")
            .add(hostname, "sha256/uyPYgclc5Jt69vKu92vci6etcBDY8UNTyrHQZJpVoZY=")
            .build()
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().addHeader(
                        "Authorization",
                        "token ${BuildConfig.TOKEN}"
                    ).build()
                )
            }
            .certificatePinner(certificatePinner)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IGithubRepository> { GithubRepository(get(), get()) }
}