package me.zhengjin.common.verification.autoconfig

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "customize.common.verification.recaptcha")
class ReCaptchaProperties {
    var secret: String = ""
    var host: GoogleReCaptchaHostType = GoogleReCaptchaHostType.WWW_RECAPTCHA_NET
}

enum class GoogleReCaptchaHostType(
    val domain: String
) {
    WWW_GOOGLE_COM("www.google.com"),
    WWW_RECAPTCHA_NET("www.recaptcha.net"),
    RECAPTCHA_GOOGLE_CN("recaptcha.google.cn")
}
