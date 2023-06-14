package com.lgguan.iot.position.entity

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class TopicInfo {
    var topicName: String? = null
    var topic: String? = null
    var mapKeys: Map<String, Any>? = null
    /** 1 String. 2. number 3 enum **/
    var properties: Map<String, Any>? = null
}