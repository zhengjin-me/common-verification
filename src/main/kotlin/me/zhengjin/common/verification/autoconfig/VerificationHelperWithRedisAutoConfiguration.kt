package me.zhengjin.common.verification.autoconfig

import me.zhengjin.common.verification.VerificationHelperWithRedis
import me.zhengjin.common.verification.service.VerificationGeneratorProvider
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.StringRedisTemplate
import javax.servlet.http.HttpServletResponse

/**
 * @version V1.0
 * @title: VerificationHelperWithRedisAutoConfiguration
 * @package me.zhengjin.common.verification.autoconfig
 * @description: 验证码助手
 * @author fangzhengjin
 * @date 2019/2/26 16:59
 */

@Configuration
@ConditionalOnClass(RedisOperations::class)
@AutoConfigureAfter(name = ["org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"])
@ConditionalOnExpression("\${customize.common.verification.redis.enable:true}")
class VerificationHelperWithRedisAutoConfiguration {

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnBean(StringRedisTemplate::class)
    @ConditionalOnMissingBean(VerificationHelperWithRedis::class)
    fun verificationHelperWithRedis(
        response: HttpServletResponse,
        redisTemplate: StringRedisTemplate,
        verificationGeneratorProviders: MutableList<VerificationGeneratorProvider>
    ): VerificationHelperWithRedis {
        return VerificationHelperWithRedis(
            response,
            redisTemplate,
            verificationGeneratorProviders
        )
    }
}
