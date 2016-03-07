package com.zzzmode.platfrom

import java.util.*

/**
 * Created by zl on 16/2/19.
 */
open class MainKt

    fun main(args: Array<String>) {

        val random=Random()
        val number=StringBuilder("13")
        for(i in IntRange(0,8)){
            number.append(random.nextInt(9))
        }

        print(number)

    }


private fun randomRoom():String{
    val  random= Random();
    return "${random.nextInt(6)+1}0${random.nextInt(3)+1}室"
}
//
//    val sExecutors = Executors.newCachedThreadPool()
//    fun async(block: () -> Unit) {
//
//        sExecutors.execute { -> block() }
//    }



