package me.zhengjin.common.verification.autoconfig

import me.zhengjin.common.verification.VerificationHelperWithGoogleReCaptcha
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @version V1.0
 * @title: VerificationHelperWithSessionAutoConfiguration
 * @package me.zhengjin.common.verification.autoconfig
 * @description: 验证码助手GoogleReCaptcha
 * @author fangzhengjin
 * @date 2019/2/26 16:59
 */
@Configuration
@EnableConfigurationProperties(ReCaptchaProperties::class)
@ConditionalOnExpression("\${customize.common.verification.recaptcha.enable:false}")
class VerificationHelperWithGoogleReCaptchaAutoConfiguration {

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean(VerificationHelperWithGoogleReCaptcha::class)
    fun verificationHelperWithGoogleReCaptcha(
        reCaptchaProperties: ReCaptchaProperties
    ): VerificationHelperWithGoogleReCaptcha {
        return VerificationHelperWithGoogleReCaptcha(reCaptchaProperties)
    }
}
