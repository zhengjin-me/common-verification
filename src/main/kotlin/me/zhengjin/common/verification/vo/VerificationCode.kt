package me.zhengjin.common.verification.vo

import me.zhengjin.common.verification.service.VerificationType
import java.awt.image.BufferedImage

/**
 * @version V1.0
 * @title: VerificationCode
 * @package me.zhengjin.common.verification.vo
 * @description:
 * @author fangzhengjin
 * @date 2019/2/26 17:02
 */
data class VerificationCode @JvmOverloads constructor(
    var verificationType: VerificationType = VerificationType.IMAGE,
    var code: String,
    var codeId: String? = null,
    var image: BufferedImage? = null
)
