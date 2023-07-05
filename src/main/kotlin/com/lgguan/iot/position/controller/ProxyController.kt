package com.lgguan.iot.position.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import jakarta.servlet.http.HttpServletRequest

@RestController
class ProxyController {
    @GetMapping("/proxydownload/{year}/{month}/{day}/{file}")
    fun download(@PathVariable year: String, @PathVariable month: String, @PathVariable day: String, @PathVariable file: String, request: HttpServletRequest): ResponseEntity<ByteArray> {
        val scheme = request.scheme  // http 或 https
        val serverName = request.serverName  // hostname
        val serverPort = request.serverPort  // port
        val url = "$scheme://$serverName:$serverPort/download/$year/$month/$day/$file"

        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(url, ByteArray::class.java)
        return ResponseEntity.ok()
            .header("Content-Type", response.headers.contentType.toString())
            .header("Content-Length", response.body?.size.toString())
            .body(response.body)
    }
}
