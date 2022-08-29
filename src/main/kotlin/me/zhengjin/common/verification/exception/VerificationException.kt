package me.zhengjin.common.verification.exception

import me.zhengjin.common.verification.service.VerificationStatus

/**
 * @version V1.0
 * @title: VerificationException
 * @package me.zhengjin.common.verification.exception
 * @description:
 * @author fangzhengjin
 * @date 2019/2/27 11:32
 */
open class VerificationException(message: String) : RuntimeException(message)

class VerificationWrongException(message: String = VerificationStatus.WRONG.message) : VerificationException(message)

class VerificationExpiredException(message: String = VerificationStatus.EXPIRED.message) : VerificationException(message)

class VerificationNotFountException(message: String = VerificationStatus.NOT_FOUNT.message) : VerificationException(message)

class VerificationUpperLimitException(message: String = VerificationStatus.UPPER_LIMIT.message) : VerificationException(message)

class VerificationFrequentOperationException(message: String = VerificationStatus.FREQUENT_OPERATION.message) : VerificationException(message)

class VerificationNotFountExpectedGeneratorProviderException(message: String = "Not found expected generator provider") : VerificationException(message)

class VerificationNotFountExpectedValidatorProviderException(message: String = "Not found expected validator provider") : VerificationException(message)
