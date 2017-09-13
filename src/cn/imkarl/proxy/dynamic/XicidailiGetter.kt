package cn.imkarl.proxy.dynamic

import cn.imkarl.proxy.http.execute
import cn.imkarl.proxy.utils.log
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

/**
 * Created by imkarl on 2017/09/09.
 */
internal object XicidailiGetter {

    /**
     * 国内普通代理
     */
    fun getChina(): HashMap<String, Int> {
        val proxys = HashMap<String, Int>()

        try {
            val document = Jsoup.parse(Request.Builder().url("http://www.xicidaili.com/nt/").build().execute().body()?.string() ?: "")
            proxys.putAll(parser(document))
        } catch (e: Throwable) {
            System.err.println("loadProxys ERROR: " + e.message)
        }

        return proxys
    }

    /**
     * 国内高匿代理
     */
    fun getChinaAnonymous(): HashMap<String, Int> {
        val proxys = HashMap<String, Int>()

        try {
            val document = Jsoup.parse(Request.Builder()
                    .url("http://www.xicidaili.com/nn/")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36")
                    .build()
                    .execute().body()?.string() ?: "")
            proxys.putAll(parser(document))
        } catch (e: Throwable) {
            System.err.println("loadProxys ERROR: " + e.message)
        }

        return proxys
    }


    private fun parser(document: Document): HashMap<String, Int> {
        val body = document.body()
        val trArrays = body.select("#ip_list").select("tr")

        val proxys = HashMap<String, Int>()
        if (trArrays.isEmpty()) {
            body.log()
        } else {
            trArrays.removeAt(0)

            trArrays.forEach { element ->
                proxys.put(element.child(1).text(), Integer.parseInt(element.child(2).text()))
                //println(element.text());
            }
        }
        return proxys
    }

}