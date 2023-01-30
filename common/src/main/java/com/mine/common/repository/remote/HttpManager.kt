package com.mine.common.repository.remote

import com.mine.common.BuildConfig
import com.mine.common.repository.remote.http.server.NetDevHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class HttpManager private constructor() {

    private lateinit var okHttpClient: OkHttpClient

    private lateinit var retrofit: Retrofit

    init {
        initClient()
    }

    private fun initClient() {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .sslSocketFactory(initSslSocketFactory()!!, intiTrustManager())
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        okHttpClient = okHttpClientBuilder.build()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getBaseUrl())
            .build()
    }

    private fun getBaseUrl(): String = NetDevHelper.instance.getTargetServer().baseUrl

    companion object {
        private const val DEFAULT_TIMEOUT: Long = 60

        private const val SSL: String = "SSL"

        @Volatile
        private var instance: HttpManager? = null

        fun getInstance(): HttpManager {
            return instance ?: synchronized(HttpManager::class.java) {
                instance ?: HttpManager().also { instance = it }
            }
        }
    }

    private fun initSslSocketFactory(): SSLSocketFactory? {
        var sslContent: SSLContext? = null
        try {
            sslContent = SSLContext.getInstance(SSL)
            val xTrustArray = arrayOf(intiTrustManager())
            sslContent.init(null, xTrustArray, SecureRandom())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sslContent?.socketFactory
    }

    private fun intiTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray<X509Certificate>()
            }

        }
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}