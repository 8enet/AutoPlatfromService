package com.zzzmode.platfrom.exception

/**
 * Created by zl on 16/5/5.
 */
class SMSPlatfromException : Exception{

    constructor(message: String?) : super(message) {
    }

    constructor(cause: Throwable) : super(cause) {
    }

    constructor(message: String?, cause: Throwable) : super(message, cause) {
    }
}