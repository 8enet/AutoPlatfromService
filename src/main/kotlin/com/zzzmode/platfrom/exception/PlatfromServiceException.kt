package com.zzzmode.platfrom.exception

/**
 * Created by zl on 16/2/16.
 */
class PlatfromServiceException : Exception {
    var code: Int = 0

    constructor(message: String?) : super(message) {
    }

    constructor(message: String?, code: Int) : super(message) {
        this.code = code
    }

    constructor(cause: Throwable) : super(cause) {
    }

    constructor(message: String?, cause: Throwable) : super(message, cause) {
    }
}
