package com.example.coffee2_app;

import java.util.Arrays;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageGenerator {

    private final int w = 500;
    private final int h = 500;
    private int seed = 0;
    private Bitmap imgBmp;
    private String name;
    private final Random rand = new Random();

    public ImageGenerator(String name) {
        this.name = name;
    }

    private void genImg() {
        // Combine ASCII values of all characters in String for seeding purposes
        for (int i = 0; i < name.length(); i++) {
            seed += name.charAt(i);
        }

        rand.setSeed(seed);

        // Random gets a number between [100, 255]
        // Clamped to a floor of 100 so we can keep
        // this is just to prevent a case of the
        // text from blending into the colour.
        int r = rand.nextInt(155) + 100;
        int g = rand.nextInt(155) + 100;
        int b = rand.nextInt(155) + 100;

        // Create an array filled with generated colour
        // so we can add it to the Bitmap
        int[] colors = new int[w * h];
        Arrays.fill(colors, Color.rgb(r, g, b));

        // Log debug for colour value
        // Log.d("colors", String.valueOf(colors[0]));

        imgBmp = Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);

        String text = String.valueOf(name.toUpperCase().charAt(0));

        // Setup Text Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(350);
        paint.setTextAlign(Paint.Align.LEFT);

        Canvas canvas = new Canvas(imgBmp);

        Rect boundingBox = new Rect();

        paint.getTextBounds(text, 0, 1, boundingBox);

        // Center character in the middle of the image
        int x = 250 - (boundingBox.width() / 2) - boundingBox.left;
        int y = 250 + (boundingBox.height() / 2) - boundingBox.bottom;

        // Draw character on Image
        canvas.drawText(text, x, y, paint);
    }

    /**
     * Change the name of the image for generation
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        genImg();
    }

    /**
     * Returns the image generated in the Object.
     * @return Deterministic Image
     */
    public Bitmap getImg() {
        this.genImg();
        return imgBmp;
    }
}
