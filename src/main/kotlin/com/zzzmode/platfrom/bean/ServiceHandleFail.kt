package com.zzzmode.platfrom.bean

import com.zzzmode.platfrom.exception.PlatfromServiceException

/**
 * Created by zl on 16/2/16.
 */
class ServiceHandleFail {

    var code: Int = 0
    var msg: String? = null

    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }

    constructor(e: PlatfromServiceException) {
        this.code = e.code
        this.msg = e.message
    }
}
