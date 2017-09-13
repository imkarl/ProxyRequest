package cn.imkarl.proxy.dynamic

import java.util.*

/**
 * Created by imkarl on 2017/09/09.
 */
object ProxyGetter {

    fun loadProxys(): Map<String, Int> {
        val proxys = HashMap<String, Int>()

        proxys.putAll(Ip181Getter.getChina())
        proxys.putAll(XicidailiGetter.getChinaAnonymous())

        return proxys
    }

}