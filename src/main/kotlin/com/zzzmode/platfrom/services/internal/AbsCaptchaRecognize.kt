package com.zzzmode.platfrom.services.internal

import com.zzzmode.platfrom.services.manager.OnlineUserManager
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by zl on 16/4/17.
 */
abstract class AbsCaptchaRecognize :ICaptchaRecognizeService{

    @Autowired
    var onlineUserManager: OnlineUserManager?=null
}