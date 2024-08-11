package com.goods.client.data.remote

import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
fun getUnsafeOkHttpClient(): OkHttpClient {
    return try {
        // Create a trust manager that trusts all certificates
        val trustAllCertificates = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return arrayOf()
            }
        }

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustAllCertificates), java.security.SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        // Create an OkHttpClient with the all-trusting trust manager
        OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCertificates)
            .hostnameVerifier { _, _ -> true } // Accept all hostnames
            .build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}