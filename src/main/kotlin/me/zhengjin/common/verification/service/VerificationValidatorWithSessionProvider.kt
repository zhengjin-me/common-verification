package me.zhengjin.common.verification.service

import me.zhengjin.common.verification.VerificationHelperWithSession
import me.zhengjin.common.verification.vo.VerificationValidateData
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * @version V1.0
 * @title: VerificationGeneratorProvider
 * @package me.zhengjin.common.verification.service
 * @description: 验证码验证提供者
 * @author fangzhengjin
 * @date 2019/2/26 16:56
 */
interface VerificationValidatorWithSessionProvider : VerificationValidatorProvider {

    /**
     * 验证码校验
     * @param session                           当前请求的session
     * @param request                           当前请求的request
     * @param response                           当前请求的response
     * @param verificationValidateData          验证信息
     * @return 如果选择验证不通过不抛出异常,则返回VerificationStatus验证状态枚举
     */
    fun render(
        request: HttpServletRequest,
        response: HttpServletResponse,
        session: HttpSession,
        verificationValidateData: VerificationValidateData
    ): VerificationStatus

    /**
     * 清理Session中的验证码信息
     */
    fun removeSessionVerificationInfo(session: HttpSession) {
        session.removeAttribute(VerificationHelperWithSession.VERIFICATION_CODE_SESSION_KEY)
        session.removeAttribute(VerificationHelperWithSession.VERIFICATION_CODE_SESSION_DATE)
        session.removeAttribute(VerificationHelperWithSession.VERIFICATION_CODE_SESSION_TYPE)
    }
}
