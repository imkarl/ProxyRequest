package cn.imkarl.proxy

import cn.imkarl.proxy.dynamic.ProxyGetter
import cn.imkarl.proxy.http.enqueue
import okhttp3.Request
import org.jsoup.Jsoup
import java.net.Proxy

/**
 * 程序主入口
 * Created by imkarl on 2017/8/22.
 */
object Main {

    private var sumSuccess = 0
    private var sumFailed = 0

    @JvmStatic fun main(args: Array<String>) {

        val url = "http://ip.cn/"
        val proxys = ProxyGetter.loadProxys()

        println("proxys.size=" + proxys.size)

        proxys.forEach { host, port ->
            val request = Request.Builder().url(url).build()
            request.enqueue(Proxy.Type.HTTP, host, port, {
                response ->

                val html = response.body()?.string() ?: ""
                var body = Jsoup.parse(html).body().text()
                val startOffset = body.indexOf("您现在的");
                var endOffset = body.indexOf("©2006-2017")
                if (startOffset >= 0) {
                    if (endOffset <= 0) {
                        endOffset = body.length
                    }
                    body = body.substring(startOffset, endOffset)
                } else {
                    body = Jsoup.parse(body).body().text()
                }
                println("proxyHttpGet body: " + body)

                sumSuccess++
            }, {
                exception ->
                println("proxyHttpGet ERROR: " + exception.message)

                sumFailed++
            }, {
                //println("success/count: "+(sumSuccess)+"/"+(sumSuccess+ sumFailed))
                if (sumSuccess + sumFailed == proxys.size) {
                    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                    println("request count: "+(sumSuccess+ sumFailed))
                    println("success=$sumSuccess   failed=$sumFailed")
                    System.exit(0)
                }
            })
        }

    }

}
