package com.lgguan.iot.position.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.text.SimpleDateFormat

/**
 * Json Object Mapper
 *
 * @author N.Liu
 **/
val objectMapper by lazy {
    ObjectMapper().apply {
        setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

        // OCPP messages contain some mandatory primitive fields (like transactionId), that are not allowed
        // to be null. any misinterpretation/mapping of these fields like "null -> 0" is a mistake.
        //
        // true story: while testing with abusive-charge-point, it sends stopTransactions where transactionId=null
        // in communication flows, where a startTransaction before causes an Exception and we cannot send a regular
        // response with a transactionId, but an error message. if we do not fail early, it will fail at the database
        // level which we want to prevent.

        // OCPP messages contain some mandatory primitive fields (like transactionId), that are not allowed
        // to be null. any misinterpretation/mapping of these fields like "null -> 0" is a mistake.
        //
        // true story: while testing with abusive-charge-point, it sends stopTransactions where transactionId=null
        // in communication flows, where a startTransaction before causes an Exception and we cannot send a regular
        // response with a transactionId, but an error message. if we do not fail early, it will fail at the database
        // level which we want to prevent.
        configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)

        registerModule(JavaTimeModule())
        dateFormat = SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'")
    }
}

fun Any.toJsonStr(): String = objectMapper.writeValueAsString(this)
