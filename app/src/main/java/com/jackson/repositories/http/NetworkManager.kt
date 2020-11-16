package com.jackson.repositories.http

import com.jackson.repositories.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkManager {

    // TCP Handshake가 완료되기까지 지속되는 시간
    const val CONNECTION_TIMEOUT: Long = 10
    // 서버로부터 응답까지의 시간이 READ_TIMEOUT을 초과하면 실패로 간주
    const val READ_TIMEOUT: Long = 10
    // 클라이언트로 부터 서버로 응답을 보내는 시간이 WRITE_TIMEOUT을 초과하면 실패로 간주
    const val WRITE_TIMEOUT: Long = 10

    val service: GithubApiService = initService()

    lateinit var client: OkHttpClient

    private fun initService(): GithubApiService {

        configureClient(OkHttpClient().newBuilder())
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor { chain ->

                // 공통 헤더 명시
                // github api header 참조 : https://developer.github.com/v3/media/
                val builder = chain.request().newBuilder()
                    // .addHeader("Authorization", "BEARER ${BuildConfig.GIT_TOKEN}")
                    .addHeader("Accept", "application/vnd.github.v3+json").build()


                chain.proceed(builder)
            }.let { builder ->

                if (BuildConfig.DEBUG) {
                    builder.addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }

                client = builder.build()

                Retrofit.Builder()
                    .baseUrl(ApiUrl.MAIN_DOMAIN)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build().let { retrofit ->
                        return retrofit.create(GithubApiService::class.java)
                    }
            }
    }

    /**
     * https 통신 시, 모든 url을 신뢰하도록 설정
     * 특정 url의 ssl 인증키가 존재할경우 아래 링크 참조
     * https://jeongupark-study-house.tistory.com/81
     * # ssl관련 참조 : https://m.blog.naver.com/ucert/221255128642
     */
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

    /**
     * 향후 기본 전달값이 존재할경우를 대비해 defaultParams를 통해 HashMap을 전달받는다.
     */
    fun defaultParams(): HashMap<String, Any?> {
        return hashMapOf<String, Any?>().apply {
            // 공통 전달 Parameters
        }
    }

}