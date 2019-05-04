package com.instant.message.service.impl

import com.google.zxing.*
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.instant.message.service.QRCodeService
import org.apache.catalina.manager.Constants.CHARSET
import org.apache.commons.codec.binary.Base64
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.StringBuilder
import java.util.*
import javax.imageio.ImageIO

@Service
class QRCodeServiceImpl : QRCodeService {

    @Throws(IOException::class)
    override fun createQRCode(content: String, width: Int, height: Int): String {

        if (!StringUtils.isEmpty(content)) {

            val os = ByteArrayOutputStream()
            val hints = HashMap<EncodeHintType, Comparable<*>>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.Q
            hints[EncodeHintType.MARGIN] = 2

            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
                val bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
                ImageIO.write(bufferedImage, "png", os)
                var stringBuilder=StringBuilder()
                stringBuilder.append("data:image/png;base64,")
                stringBuilder.append(Base64.encodeBase64String(os.toByteArray()))

                return String(stringBuilder)
            } catch (e: WriterException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                    os.flush()
                    os.close()
            }

        }
        return ""
    }

    override fun decode(content: String): String {

        var input: InputStream? = null
        try {
            val decoder = Base64()
            //Base64解码
            val b = decoder.decode(content.replace("data:image/png;base64,", ""))
            for (i in b.indices) {
                if (b[i] < 0) {//调整异常数据
                    b[i]= (b[i]+256).toByte()
                }
            }
            input = ByteArrayInputStream(b)
            val image = ImageIO.read(input)

            val source = BufferedImageLuminanceSource(image)
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            val result: Result
            val hints = Hashtable<DecodeHintType,String>()
            hints.put(DecodeHintType.CHARACTER_SET, CHARSET)
            try {
                result = MultiFormatReader().decode(bitmap, hints)
                return result.text
            } catch (e: NotFoundException) {
                e.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        }
        return ""

    }
}