package me.zhengjin.common.verification.service.impl.generator

import me.zhengjin.common.utils.FileUtils
import me.zhengjin.common.verification.service.VerificationGeneratorProvider
import me.zhengjin.common.verification.service.VerificationType
import me.zhengjin.common.verification.vo.VerificationCode
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.util.Random

/**
 * @version V1.0
 * @title: DefaultImageVerificationGeneratorProvider
 * @package me.zhengjin.common.verification.service.impl.generator
 * @description:
 * @author fangzhengjin
 * @date 2019/2/26 17:39
 */
class DefaultImageVerificationGeneratorProvider : VerificationGeneratorProvider {
    /**
     * 是否支持该类型的验证码生成
     */
    override fun isSupports(verificationType: VerificationType): Boolean {
        return verificationType == VerificationType.IMAGE
    }

    companion object {
        // 定义图片的width
        private const val width = 130

        // 定义图片的height
        private const val height = 50

        // 定义图片上显示验证码的个数
        private const val codeCount = 4
        private const val blank = 10
        private const val fontHeight = 40
        private const val codeY = 40

        private val codeSequence = arrayOf(
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "J",
            "K",
            "M",
            "N",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
        )

        @JvmStatic
        val logger = LoggerFactory.getLogger(this::class.java)

        private fun getRandColor(fc: Int, bc: Int): Color { // 给定范围获得随机颜色
            var localFc = fc
            var localBc = bc
            val random = Random()
            if (localFc > 255)
                localFc = 255
            if (localBc > 255)
                localBc = 255
            val r = localFc + random.nextInt(localBc - localFc)
            val g = localFc + random.nextInt(localBc - localFc)
            val b = localFc + random.nextInt(localBc - localFc)
            return Color(r, g, b)
        }

        private fun getFont(fontHeight: Float): Font? {
            // font[0] = new Font("Ravie", Font.BOLD, fontHeight);
            // font[1] = new Font("Antique Olive Compact", Font.BOLD, fontHeight);
            // font[2] = new Font("Forte", Font.BOLD, fontHeight);
            // font[0] = new Font("Wide Latin", Font.PLAIN, fontHeight);
            // font[4] = new Font("Gill Sans Ultra Bold", Font.BOLD, fontHeight);
            val createFont = Font.createFont(
                Font.TRUETYPE_FONT,
                FileUtils.getFileAsInputStream("classpath:font/AlibabaSans-Medium.otf")
            )
            return createFont.deriveFont(Font.BOLD, fontHeight)
        }
    }

    override fun render(): VerificationCode {
        val buffImg = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        // Graphics2D gd = buffImg.createGraphics();
        // Graphics2D gd = (Graphics2D) buffImg.getGraphics();
        val gd = buffImg.graphics
        // 创建一个随机数生成器类
        val random = Random()

        gd.color = getRandColor(200, 250)
        gd.fillRect(0, 0, width, height)

        // 创建字体，字体的大小应该根据图片的高度来定。
        // Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.font = getFont(fontHeight.toFloat())

        // 画边框。
        // gd.setColor(Color.BLACK);
        // gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.color = getRandColor(160, 200)
        for (i in 0..159) {
            val x = random.nextInt(width)
            val y = random.nextInt(height)
            val xl = random.nextInt(20)
            val yl = random.nextInt(20)
            gd.drawLine(x, y, x + xl, y + yl)
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        val randomCode = StringBuffer()
        // int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (i in 0 until codeCount) {
            // 得到随机产生的验证码数字。
            val code = codeSequence[random.nextInt(codeSequence.size)]
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            // red = random.nextInt(255);
            // green = random.nextInt(255);
            // blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            gd.color = Color(30 + random.nextInt(80), 30 + random.nextInt(80), 30 + random.nextInt(80))
            gd.drawString(code, 30 * i + blank, codeY)

            // 将产生的四个随机数组合在一起。
            randomCode.append(code)
        }

        return VerificationCode(code = randomCode.toString(), image = buffImg)
    }
}
