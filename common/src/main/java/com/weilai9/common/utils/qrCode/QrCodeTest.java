package com.weilai9.common.utils.qrCode;

import javax.annotation.Resource;
import java.util.Random;


public class QrCodeTest {
    public static void main(String[] args) throws Exception {

        // 存放在二维码中的内容
        String text = "186130330972";
        // 嵌入二维码的图片路径
//        String imgPath = "D:/JAVA/9a504fc2d5628535a675deb89bef76c6a6ef6395.png";
        // 生成的二维码的路径及名称
        String file = new Random().nextInt(99999999)+".jpg";
        String destPath = "D:/JAVA/"+file;
        //生成二维码
        QRCodeUtil.encode(text, null, destPath, true);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);

        System.out.println(Path2FileUtil.getMulFileByPath(destPath));
        System.out.println(file);


    }

}
