package com.radja.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class QRCodeGenerator {
    
    public static BufferedImage generateQRCodeImage(String text, int size) throws Exception {
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // Level koreksi error
        hintMap.put(EncodeHintType.MARGIN, 1); // Margin QR Code

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hintMap);

        int width = bitMatrix.getWidth();
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, width);
        graphics.setColor(Color.BLACK);

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }

        return image;
    }
}
