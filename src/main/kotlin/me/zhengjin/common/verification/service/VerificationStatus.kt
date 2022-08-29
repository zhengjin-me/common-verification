package me.zhengjin.common.verification.service

/**
 * @version V1.0
 * @title: VerificationStatus
 * @package me.zhengjin.common.verification.service
 * @description:
 * @author fangzhengjin
 * @date 2019/2/26 17:26
 */
enum class VerificationStatus(val message: String) {
    NOT_FOUNT("验证码已失效"),
    SUCCESS("验证通过"),
    WRONG("验证码错误"),
    EXPIRED("验证码已过期"),
    FREQUENT_OPERATION("操作频繁"),
    UPPER_LIMIT("操作次数已达上限")
}
