package com.zzzmode.platfrom

import com.zzzmode.platfrom.services.ToolsService
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(AppPlatfromServiceApplication::class))
@WebAppConfiguration
class AppPlatfromServiceApplicationTests {

	companion object{
		private val logger=LoggerFactory.getLogger(AppPlatfromServiceApplicationTests::class.java)
	}


    @Autowired
    var toolsService : ToolsService?=null

	@Test
	fun contextLoads() {
		logger.debug("contextLoads ")
	}

	@Test
	fun testProxys(){

	}

}
