package com.zzzmode.platfrom.util

import java.io.File
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by zl on 16/3/6.
 */
class Utils {
    companion object {
        final val sNextGeneratedId = AtomicInteger(1);
    }

}

fun Utils.Companion.getFile(path: String?): File? {
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

fun Utils.Companion.generateUserId(): Int {
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
