package com.zzzmode.platfrom.util

import java.io.File
import java.nio.file.Paths

/**
 * Created by zl on 16/3/6.
 */
class FileUtils {
    companion object {}

}
fun FileUtils.Companion.getFile(path: String?): File? {
    Paths.get(path)?.apply {
        if(this.isAbsolute){
            return File(path)
        }else{
            ClassLoader.getSystemResource(path)?.apply {
                this.toURI()?.apply {
                    return File(this);
                }
            }
        }
    }
    return null
}
