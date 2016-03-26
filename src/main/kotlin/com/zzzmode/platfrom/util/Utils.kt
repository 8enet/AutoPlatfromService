package com.zzzmode.platfrom.util

import java.io.File
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * Created by zl on 16/3/6.
 */
class Utils {
    companion object {
        private final val sNextGeneratedId = AtomicInteger(1);

        fun getFile(path: String?): File? {
            Paths.get(path)?.apply {
                if (this.isAbsolute) {
                    return File(path)
                } else {
                    ClassLoader.getSystemResource(path)?.apply {
                        this.toURI()?.apply {
                            return File(this);
                        }
                    }
                }
            }
            return null
        }

        fun generateUserId(): Int {
            while (true) {
                val result = sNextGeneratedId.get();
                var newValue = result + 1;
                if (newValue > Int.MAX_VALUE) {
                    newValue = 1
                }
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }

        }


    }

}

private  val sExecutor=Executors.newCachedThreadPool(MyThreadFactory())
private  class  MyThreadFactory: ThreadFactory{

    companion object{
        private val poolNumber = AtomicInteger(1)
    }

    constructor(){
        val s = System.getSecurityManager()
        group = if (s != null)
            s.threadGroup
        else
            Thread.currentThread().threadGroup
        namePrefix = "autoPS-pool-" +
                poolNumber.andIncrement +
                "-thread-"
    }

    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String

    override fun newThread(r: Runnable): Thread? {
        val t = Thread(group, r,
                namePrefix + threadNumber.andIncrement,
                0)
        if (t.isDaemon)
            t.isDaemon = false
        if (t.priority != Thread.NORM_PRIORITY)
            t.priority = Thread.NORM_PRIORITY

        return t;
    }
}

/**
 * 异步执行
 */
fun async(block: () -> Unit): Future<*> {
    return  sExecutor.submit(block)
}