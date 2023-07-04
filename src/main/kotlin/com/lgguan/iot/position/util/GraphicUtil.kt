package com.lgguan.iot.position.util

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class Point(
    @JsonProperty("x")
    @field:Schema(required = true)
    val x: Double,
    @JsonProperty("y")
    @field:Schema(required = true)
    val y: Double
)

infix fun Point.inRange(polygon: List<Point>): Boolean {
    var result = false
    var j = polygon.size - 1
    for (i in polygon.indices) {
        if (polygon[i].y < this.y && polygon[j].y >= this.y ||
            polygon[j].y < this.y && polygon[i].y >= this.y
        ) {
            if (polygon[i].x + (this.y - polygon[i].y) /
                (polygon[j].y - polygon[i].y) *
                (polygon[j].x - polygon[i].x) < this.x
            ) {
                result = !result
            }
        }
        j = i
    }
    return result
}
