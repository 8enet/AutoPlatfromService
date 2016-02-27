package com.zzzmode.platfrom.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedOutputStream

import java.text.DateFormat
import java.util.Date

/**
 * Created by zl on 16/2/16.
 */
@RestController
@RequestMapping("/")
class IndexController : BaseController(){

    companion object {

        private val logger = LoggerFactory.getLogger(IndexController::class.java)
    }

    @RequestMapping("/")
    fun hello(@RequestHeader headers: HttpHeaders): String {
        return "server ok !" + DateFormat.getInstance().format(Date()) + "\n" + headers.getFirst(HttpHeaders.USER_AGENT)
    }


    @RequestMapping(value = "/post",method = arrayOf(RequestMethod.POST))
    fun hello(@RequestParam(value = "data") data:String,
              @RequestParam(value = "id") id:String,
              @RequestParam("file")  file: MultipartFile,
              @RequestParam("file2")  file2: MultipartFile): String {

        logger.debug(file.originalFilename)

        logger.debug(file2.originalFilename)

        return "server ok ! ${data} , ${id} " ;
    }
}
