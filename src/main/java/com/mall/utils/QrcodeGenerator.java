package com.mall.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mall.common.Constant;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrcodeGenerator {
    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) throws IOException, WriterException {
        generateQRCodeImage("hello word",200,200, "D:\\\\File\\\\GitHub\\\\mall-dev\\\\src\\\\main\\\\resources\\\\static\\\\QRTest.png");
    }
}
