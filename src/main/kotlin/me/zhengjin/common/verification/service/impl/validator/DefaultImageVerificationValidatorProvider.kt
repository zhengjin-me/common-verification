package me.zhengjin.common.verification.service.impl.validator

import me.zhengjin.common.verification.exception.VerificationExpiredException
import me.zhengjin.common.verification.exception.VerificationNotFountException
import me.zhengjin.common.verification.exception.VerificationWrongException
import me.zhengjin.common.verification.service.VerificationStatus
import me.zhengjin.common.verification.service.VerificationType
import me.zhengjin.common.verification.service.VerificationValidatorWithSessionProvider
import me.zhengjin.common.verification.vo.VerificationValidateData
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * @version V1.0
 * @title: DefaultImageVerificationGeneratorProvider
 * @package me.zhengjin.common.verification.service.impl.validator
 * @description:
 * @author fangzhengjin
 * @date 2019/2/26 17:39
 */
class DefaultImageVerificationValidatorProvider : VerificationValidatorWithSessionProvider {
    /**
     * 是否支持该类型的验证码验证
     */
    override fun isSupports(verificationType: VerificationType): Boolean {
        return verificationType == VerificationType.IMAGE
    }

    /**
     * 验证码校验
     * @param session                           当前请求的session
     * @param request                           当前请求的request
     * @param response                          当前请求的response
     * @param verificationValidateData          验证信息
     * @return 如果选择验证不通过不抛出异常,则返回VerificationStatus验证状态枚举
     */
    @Throws(
        VerificationNotFountException::class,
        VerificationWrongException::class,
        VerificationExpiredException::class
    )
    override fun render(
        request: HttpServletRequest,
        response: HttpServletResponse,
        session: HttpSession,
        verificationValidateData: VerificationValidateData
    ): VerificationStatus {
        if (verificationValidateData.isExpire) {
            removeSessionVerificationInfo(session)
            return if (verificationValidateData.throwException) throw VerificationExpiredException() else VerificationStatus.EXPIRED
        }
        if (!verificationValidateData.userInputCode.equals(verificationValidateData.code, true)) {
            // 如果是图片类型验证码 并且没有设置cleanupVerificationInfoWhenWrong则默认清理
            if (verificationValidateData.cleanupVerificationInfoWhenWrong) removeSessionVerificationInfo(session)
            return if (verificationValidateData.throwException) throw VerificationWrongException() else VerificationStatus.WRONG
        }
        removeSessionVerificationInfo(session)
        return VerificationStatus.SUCCESS
    }
}
