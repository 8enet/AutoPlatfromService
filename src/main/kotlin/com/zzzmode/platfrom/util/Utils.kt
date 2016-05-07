package com.zzzmode.platfrom.util

import java.io.File
import java.nio.file.Paths
import java.util.concurrent.*
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
    }

}

private  val sExecutor=Executors.newCachedThreadPool(MyThreadFactory("autoPS-pool-"))
private  class  MyThreadFactory: ThreadFactory{

    companion object{
        private val poolNumber = AtomicInteger(1)
    }

    constructor(poolName:String){
        val s = System.getSecurityManager()
        group = if (s != null)
            s.threadGroup
        else
            Thread.currentThread().threadGroup
        namePrefix = poolName +
                poolNumber.andIncrement +
                "-thread-"
    }

    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix:String

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
fun async(body: () -> Unit): Future<*> {

    return  sExecutor.submit(body)
}

fun <T> invokAll(callables: List<Callable<T>>):T?{
    return  sExecutor.invokeAny(callables)
}

/**
 * 新建一个线程池,需要手动关闭
 * @param poolName 线程池名称
 * @param taskSize 任务数量
 */
fun newTemporaryExecutor(poolName:String,taskSize:Int): ExecutorService? {
    var service:ExecutorService?=null
    if(taskSize > 5){
        service=Executors.newFixedThreadPool(5,MyThreadFactory(poolName))
    }else{
        service=Executors.newCachedThreadPool(MyThreadFactory(poolName))
    }
    return service
}

public inline fun Any?.isNull(): Boolean = this == null

public inline fun Any?.isNotNull(): Boolean = this != null