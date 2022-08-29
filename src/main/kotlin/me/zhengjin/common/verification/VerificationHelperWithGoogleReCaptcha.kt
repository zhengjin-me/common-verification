package me.zhengjin.common.verification

import me.zhengjin.common.verification.autoconfig.ReCaptchaProperties
import me.zhengjin.common.verification.exception.VerificationException
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.time.Duration

/**
 * @version V1.0
 * @title: VerificationHelperWithGoogleReCaptcha
 * @package me.zhengjin.common.verification
 * @description: 验证码助手
 * @author fangzhengjin
 * @date 2019/2/26 16:34
 */
class VerificationHelperWithGoogleReCaptcha(
    private val reCaptchaProperties: ReCaptchaProperties
) {

    companion object {
        @JvmStatic
        private val restTemplate: RestTemplate = RestTemplateBuilder()
            .setReadTimeout(Duration.ofSeconds(30))
            .setConnectTimeout(Duration.ofSeconds(30))
            .build()

        @JvmStatic
        public val errorCodes = hashMapOf(
            "missing-input-secret" to "The secret parameter is missing",
            "invalid-input-secret" to "The secret parameter is invalid or malformed",
            "missing-input-response" to "The response parameter is missing",
            "invalid-input-response" to "The response parameter is invalid or malformed",
            "bad-request" to "The request is invalid or malformed",
            "timeout-or-duplicate" to "The response is no longer valid: either is too old or has been used previously"
        )
    }

    class ResponseData(
        var challengeTs: String? = null,
        var score: Double? = null,
        var errorCodes: List<String> = ArrayList(),
        var hostname: String? = null,
        var action: String? = null,
        var success: Boolean = false
    ) {

        companion object {
            @JvmStatic
            fun valueOf(data: HashMap<String, Any>): ResponseData {
                if (data.containsKey("error-codes")) {
                    return ResponseData(
                        success = false,
                        errorCodes = data["error-codes"] as List<String>
                    )
                }
                if (data["success"] == true) {
                    return ResponseData(
                        success = true,
                        hostname = data["hostname"] as String?,
                        score = data["score"] as Double?,
                        action = data["action"] as String?,
                        challengeTs = data["challenge_ts"] as String
                    )
                }
                return ResponseData(success = false)
            }
        }
    }

    /**
     * 验证码校验
     * @param token     验证令牌
     * @param secret    通信秘钥
     */
    @JvmOverloads
    fun validate(token: String, secret: String = reCaptchaProperties.secret): ResponseData {
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        // 提交参数
        val params = LinkedMultiValueMap<String, String>()
        params["secret"] = secret
        params["response"] = token
        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)
        val responseEntity = restTemplate.postForEntity(
            "https://${reCaptchaProperties.host.domain}/recaptcha/api/siteverify",
            request,
            HashMap::class.java
        )
        if (responseEntity.statusCode == HttpStatus.OK) {
            val response = responseEntity.body ?: throw VerificationException("request error")
            return ResponseData.valueOf(response as HashMap<String, Any>)
        } else {
            throw VerificationException("request error statusCode [${responseEntity.statusCode}]")
        }
    }
}
