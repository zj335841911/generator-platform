package com.qdkj.firstpro.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;


/**
 * @author pibigstar
 * @desc 海报生成工具
 **/
public class PosterUtil {

    public static void main(String[] args){
        drawPoster();
    }

    /**
     * @Author:pibigstar
     * @Description: 画海报
     */
    public static void drawPoster(){

    }

    /**
     * 获取带有logo的二维码图片
     * @param QRcode 跳转的url
     * @param logoUrl logo的访问地址
     * @param response
     */
    public static void getLogoCodeImg(String QRcode, String logoUrl, HttpServletResponse response) {
        try {
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(QRcode, BarcodeFormat.QR_CODE, 600, 600, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage QRimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    QRimg.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
                Image srcLogo = ImageIO.read(new URL(logoUrl));
                int srcLogoWidth = 600 / 4;//获取logo的宽
                int srcLogoHeight = 600 / 4;//获logo片的高
                Graphics2D graph = QRimg.createGraphics();
                int x = (QRimg.getWidth() - srcLogoWidth) / 2;
                int y = (QRimg.getHeight() - srcLogoHeight) / 2;
                graph.drawImage(srcLogo, x, y, srcLogoWidth, srcLogoHeight, null);
                graph.dispose();
            // 输出图片
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            // 将图像输出到Servlet输出流中。
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write(QRimg, "jpeg", sos);
            sos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取resources下的文件输入流
     */
    private static InputStream getInputStream(String fileName) {
        return PosterUtil.class.getClassLoader().getResourceAsStream(fileName);
    }
}

