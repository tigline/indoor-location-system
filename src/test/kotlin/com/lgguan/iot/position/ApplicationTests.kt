package com.lgguan.iot.position

import com.lgguan.iot.position.util.ResourceUtil
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {

    @Test
    fun contextLoads() {
        System.out.println(ResourceUtil().readResourceJson("model/AOA01.json"))
    }

}
