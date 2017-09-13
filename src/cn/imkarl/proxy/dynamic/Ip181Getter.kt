package cn.imkarl.proxy.dynamic

import cn.imkarl.proxy.http.execute
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.HashMap

/**
 * Created by imkarl on 2017/09/09.
 */
internal object Ip181Getter {

    fun getChina(): HashMap<String, Int> {
        val proxys = HashMap<String, Int>()

        try {
            val document = Jsoup.parse(Request.Builder().url("http://www.ip181.com/").build().execute().body()?.string() ?: "")
            val body = document.body()
            val trArrays = body.select(".table-hover").select("tr")
            trArrays.removeAt(0)
            trArrays.forEach { element ->
                proxys.put(element.child(0).text(), Integer.parseInt(element.child(1).text()))
                //println(element.text());
            }
        } catch (e: Throwable) {
            System.err.println("loadProxys ERROR: " + e.message)
        }

        return proxys
    }

}