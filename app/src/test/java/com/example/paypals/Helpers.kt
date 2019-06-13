package com.example.paypals

import com.github.kittinunf.fuel.core.Body
import com.github.kittinunf.fuel.core.Response
import java.io.ByteArrayInputStream
import java.io.OutputStream
import java.net.URL

fun getTestFuelResponse(url: URL, jsonString: String): Response =  Response(
    url = url,
    responseMessage = jsonString,
    body = object : Body {
        override val length = jsonString.length.toLong()
        override fun asString(contentType: String?) = jsonString
        override fun isConsumed() = false
        override fun isEmpty() = false
        override fun toByteArray() = jsonString.toByteArray()
        override fun toStream() = ByteArrayInputStream(jsonString.toByteArray(Charsets.UTF_8))
        override fun writeTo(outputStream: OutputStream) = 2L
    }
)