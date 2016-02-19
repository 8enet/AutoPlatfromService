package com.zzzmode.platfrom

import org.springframework.boot.ExitCodeGenerator

class ExitException : RuntimeException(), ExitCodeGenerator {

    override fun getExitCode(): Int {
        return 10
    }

}