package com.instant.message.service

import java.io.IOException

interface QRCodeService {
    @Throws(IOException::class)
     fun createQRCode(content: String, width: Int, height: Int): String

     fun decode(content: String): String
}