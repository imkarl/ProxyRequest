package cn.imkarl.proxy.http

import okhttp3.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * 代理HTTP客户端
 * Created by imkarl on 2017/08/29.
 */

private val HTTP_TIMEOUT = 20 * 1000L
private val sHttpClientBuilder = OkHttpClient.Builder().connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)

fun Request.execute(type: Proxy.Type, host: String, port: Int): Response {
    return this.execute(Proxy(type, InetSocketAddress(host, port)))
}
fun Request.execute(proxy: Proxy? = null): Response {
    // set proxy
    sHttpClientBuilder.proxy(proxy)
    // do request
    return sHttpClientBuilder.build().newCall(this).execute()
}

fun Request.enqueue(type: Proxy.Type, host: String, port: Int,
                    success: (Response)->Unit,
                    failed: ((IOException)->Unit)?=null,
                    complete: (()->Unit)?=null) {
    this.enqueue(Proxy(type, InetSocketAddress(host, port)), success, failed, complete)
}
fun Request.enqueue(proxy: Proxy? = null,
                    success: (Response)->Unit,
                    failed: ((IOException)->Unit)?=null,
                    complete: (()->Unit)?=null) {
    // set proxy
    sHttpClientBuilder.proxy(proxy)
    // do request
    return sHttpClientBuilder.build().newCall(this).enqueue(object: Callback {
        override fun onResponse(call: Call, response: Response) {
            try {
                success.invoke(response)
            } catch (e: IOException) {
                failed?.invoke(e)
            }
            complete?.invoke()
        }
        override fun onFailure(call: Call, exception: IOException) {
            failed?.invoke(exception)
            complete?.invoke()
        }
    })
}
