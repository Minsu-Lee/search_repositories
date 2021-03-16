package com.jackson.repositoriestest.di

import com.google.gson.GsonBuilder
import com.jackson.repositoriestest.BuildConfig
import com.jackson.repositoriestest.http.ApiUrl
import com.jackson.repositoriestest.http.GithubApiService
import com.jackson.repositoriestest.http.NetworkManager
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// TCP Handshake가 완료되기까지 지속되는 시간
private const val CONNECTION_TIMEOUT: Long = 10
// 서버로부터 응답까지의 시간이 READ_TIMEOUT을 초과하면 실패로 간주
private const val READ_TIMEOUT: Long = 10
// 클라이언트로 부터 서버로 응답을 보내는 시간이 WRITE_TIMEOUT을 초과하면 실패로 간주
private const val WRITE_TIMEOUT: Long = 10

val networkModule = module {

    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }

    single { GsonBuilder().create() }

    single {
        configureClient(OkHttpClient().newBuilder())
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get())
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }).build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(ApiUrl.MAIN_DOMAIN)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().apply {
                header("Accept", "application/vnd.github.mercy-preview+json")
                header("Content-Type", "Content-Type: application/json; charset=utf8")
            }.build())
        }
    }

}

private fun configureClient(builder: OkHttpClient.Builder): OkHttpClient.Builder {
    arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate>? {
            return arrayOf()
        }
    }).let { certs ->

        var ctx: SSLContext? = null
        try {
            ctx = SSLContext.getInstance("TLS")
            ctx.init(null, certs, SecureRandom())
        } catch(e: GeneralSecurityException) {
            e.printStackTrace()
        }

        /**
         * 참고 : https://gist.github.com/maiconhellmann/c61a533eca6d41880fd2b3f8459c07f7
         */
        ctx?.let {
            try {
                builder.sslSocketFactory(it.socketFactory, certs[0] as X509TrustManager)
                    .hostnameVerifier { s, sslSession -> true }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return builder
    }
}