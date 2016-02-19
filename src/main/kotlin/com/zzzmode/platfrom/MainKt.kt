package com.zzzmode.platfrom

import java.io.ByteArrayOutputStream
import java.net.Socket
import java.util.concurrent.Executors

/**
 * Created by zl on 16/2/19.
 */
open class MainKt

    fun main(args: Array<String>) {
        val socket = Socket("127.0.0.1", 8080)


        socket.outputStream.write("GET /phone/13800138000 \r\n\r\n\r\n".toByteArray())
        println("----====")
        async() {

            println("----")
            val baos = ByteArrayOutputStream(8192)

            val buff = ByteArray(1024)
            var len: Int = -1;
            len = socket.inputStream.read(buff)
            while ( len != -1) {
                baos.write(buff, 0, len)
                len = socket.inputStream.read(buff)
            }

            println(Thread.currentThread())
            println(String(baos.toByteArray()))
        }

    }

    val sExecutors = Executors.newCachedThreadPool()
    fun async(block: () -> Unit) {

        sExecutors.execute { -> block() }
    }



