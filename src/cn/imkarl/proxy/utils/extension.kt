package cn.imkarl.proxy.utils

import java.util.*

fun Any.log() {
    if (this is Throwable) {
        this.printStackTrace(System.err)
        return
    }
    if (this is ByteArray) {
        val str = Arrays.toString(this)
        System.out.println("ByteArray:${str}");
        return
    }
    System.out.println(this);
}
