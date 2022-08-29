package me.zhengjin.common.verification.autoconfig

import me.zhengjin.common.verification.VerificationHelperWithSession
import me.zhengjin.common.verification.service.VerificationGeneratorProvider
import me.zhengjin.common.verification.service.VerificationValidatorWithSessionProvider
import me.zhengjin.common.verification.service.impl.generator.DefaultImageVerificationGeneratorProvider
import me.zhengjin.common.verification.service.impl.generator.DefaultMailVerificationGeneratorProvider
import me.zhengjin.common.verification.service.impl.validator.DefaultImageVerificationValidatorProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * @version V1.0
 * @title: VerificationHelperWithSessionAutoConfiguration
 * @package me.zhengjin.common.verification.autoconfig
 * @description: 验证码助手
 * @author fangzhengjin
 * @date 2019/2/26 16:59
 */
@Configuration
@ConditionalOnExpression("\${customize.common.verification.session.enable:true}")
class VerificationHelperWithSessionAutoConfiguration {

    /**
     * 图形验证码
     */
    @Bean
    @ConditionalOnMissingBean(DefaultImageVerificationGeneratorProvider::class)
    fun defaultImageVerificationGeneratorProvider(): VerificationGeneratorProvider {
        return DefaultImageVerificationGeneratorProvider()
    }

    /**
     * 数字验证码
     */
    @Bean
    @ConditionalOnMissingBean(DefaultMailVerificationGeneratorProvider::class)
    fun defaultMailVerificationGeneratorProvider(): VerificationGeneratorProvider {
        return DefaultMailVerificationGeneratorProvider()
    }

    @Bean
    @ConditionalOnMissingBean(VerificationValidatorWithSessionProvider::class)
    fun defaultVerificationValidateProvider(): VerificationValidatorWithSessionProvider {
        return DefaultImageVerificationValidatorProvider()
    }

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean(VerificationHelperWithSession::class)
    fun verificationHelperWithSession(
        request: HttpServletRequest,
        response: HttpServletResponse,
        session: HttpSession,
        verificationGeneratorProviders: MutableList<VerificationGeneratorProvider>,
        verificationValidatorProviders: MutableList<VerificationValidatorWithSessionProvider>
    ): VerificationHelperWithSession {
        return VerificationHelperWithSession(
            request,
            response,
            session,
            verificationGeneratorProviders,
            verificationValidatorProviders
        )
    }
}
