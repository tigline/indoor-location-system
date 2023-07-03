package com.lgguan.iot.position.external

import retrofit2.http.GET
import retrofit2.http.Query

interface AcServerApi {
    @GET("/ac/location")
    suspend fun getLocation(@Query("cmd") cmd: String = "poslist"): List<LocationInfoRes>

    @GET("/ac/location")
    suspend fun updateGatewayLocation(
        @Query("id") gateway: String,
        @Query("x") x: Double? = null,
        @Query("y") y: Double? = null,
        @Query("z") z: Double? = null,
        @Query("angle") angle: Double? = null,
        @Query("group") group: String? = null,
        @Query("cmd") cmd: String = "edit_stationaoa"
    ): Any
}
