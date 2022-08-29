package me.zhengjin.common.verification.service

/**
 * @version V1.0
 * @title: VerificationGeneratorProvider
 * @package me.zhengjin.common.verification.service
 * @description: 验证码验证提供者
 * @author fangzhengjin
 * @date 2019/2/26 16:56
 */
interface VerificationValidatorProvider {

    /**
     * 是否支持该类型的验证码验证
     */
    fun isSupports(verificationType: VerificationType): Boolean
}
