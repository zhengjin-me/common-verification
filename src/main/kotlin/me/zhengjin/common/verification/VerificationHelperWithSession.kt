package me.zhengjin.common.verification

import me.zhengjin.common.verification.exception.VerificationExpiredException
import me.zhengjin.common.verification.exception.VerificationNotFountException
import me.zhengjin.common.verification.exception.VerificationNotFountExpectedGeneratorProviderException
import me.zhengjin.common.verification.exception.VerificationNotFountExpectedValidatorProviderException
import me.zhengjin.common.verification.exception.VerificationWrongException
import me.zhengjin.common.verification.service.VerificationGeneratorProvider
import me.zhengjin.common.verification.service.VerificationStatus
import me.zhengjin.common.verification.service.VerificationType
import me.zhengjin.common.verification.service.VerificationValidatorWithSessionProvider
import me.zhengjin.common.verification.vo.VerificationCode
import me.zhengjin.common.verification.vo.VerificationValidateData
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.LocalDateTime
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * @version V1.0
 * @title: VerificationHelperWithSession
 * @package me.zhengjin.common.verification
 * @description: 验证码助手
 * @author fangzhengjin
 * @date 2019/2/26 16:34
 */
class VerificationHelperWithSession(
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val session: HttpSession,
    private val verificationGeneratorProviders: MutableList<VerificationGeneratorProvider>,
    private val verificationValidatorProviders: MutableList<VerificationValidatorWithSessionProvider>
) {

    companion object {
        const val VERIFICATION_CODE_SESSION_KEY = "VERIFICATION_CODE_SESSION_KEY"
        const val VERIFICATION_CODE_SESSION_DATE = "VERIFICATION_CODE_SESSION_DATE"
        const val VERIFICATION_CODE_SESSION_TYPE = "VERIFICATION_CODE_SESSION_TYPE"
    }

    /**
     * 生成验证码
     */
    @Throws(VerificationNotFountExpectedGeneratorProviderException::class)
    @JvmOverloads
    fun render(verificationType: VerificationType = VerificationType.IMAGE, imageAutoWrite: Boolean = true): VerificationCode {
        verificationGeneratorProviders.forEach {
            if (it.isSupports(verificationType)) {
                val verificationCode = it.render()
                session.setAttribute(VERIFICATION_CODE_SESSION_KEY, verificationCode.code)
                session.setAttribute(VERIFICATION_CODE_SESSION_DATE, LocalDateTime.now())
                session.setAttribute(VERIFICATION_CODE_SESSION_TYPE, verificationCode.verificationType)

                // 只有图片验证码才执行流输出
                if (imageAutoWrite && verificationCode.image != null) {
                    response.setDateHeader(HttpHeaders.EXPIRES, 0L)
                    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate")
                    response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0")
                    response.setHeader(HttpHeaders.PRAGMA, "no-cache")
                    response.contentType = MediaType.IMAGE_JPEG_VALUE
                    response.outputStream.use {
                        ImageIO.write(verificationCode.image, "JPEG", it)
                    }
                }

                return verificationCode
            }
        }
        throw VerificationNotFountExpectedGeneratorProviderException()
    }

    /**
     * 验证码校验
     * @param code                              用户输入的验证码
     * @param expireInSeconds                   验证码有效期(秒),默认60
     * @param cleanupVerificationInfoWhenWrong  验证码输入错误时,是否作废之前的验证码信息,默认false
     * @param throwException                    验证不通过时,是否抛出异常,默认false
     * @return 如果选择验证不通过不抛出异常,则返回VerificationStatus验证状态枚举
     */
    @JvmOverloads
    @Throws(
        VerificationNotFountException::class,
        VerificationWrongException::class,
        VerificationExpiredException::class,
        VerificationNotFountExpectedValidatorProviderException::class
    )
    fun validate(
        code: String,
        expireInSeconds: Long = 60,
        cleanupVerificationInfoWhenWrong: Boolean = false,
        throwException: Boolean = false
    ): VerificationStatus {
        val sessionCode = (
            session.getAttribute(VERIFICATION_CODE_SESSION_KEY)
                ?: return if (throwException) throw VerificationNotFountException() else VerificationStatus.NOT_FOUNT
            ) as String
        val codeCreatedTime = (
            session.getAttribute(VERIFICATION_CODE_SESSION_DATE)
                ?: return if (throwException) throw VerificationNotFountException() else VerificationStatus.NOT_FOUNT
            ) as LocalDateTime
        val verificationType = (
            session.getAttribute(VERIFICATION_CODE_SESSION_TYPE)
                ?: return if (throwException) throw VerificationNotFountException() else VerificationStatus.NOT_FOUNT
            ) as VerificationType

        verificationValidatorProviders.forEach {
            if (it.isSupports(verificationType)) {
                return it.render(
                    request,
                    response,
                    session,
                    VerificationValidateData(
                        sessionCode,
                        code,
                        verificationType,
                        LocalDateTime.now().isAfter(codeCreatedTime.plusSeconds(expireInSeconds)),
                        cleanupVerificationInfoWhenWrong,
                        throwException
                    )
                )
            }
        }
        throw VerificationNotFountExpectedValidatorProviderException()
    }
}
