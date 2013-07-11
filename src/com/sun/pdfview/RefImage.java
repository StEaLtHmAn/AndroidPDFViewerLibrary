package com.sun.pdfview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;


/**
 * A BufferedImage subclass that holds a strong reference to its graphics 
 * object.  This means that the graphics will never go away as long as 
 * someone holds a reference to this image, and createGraphics() and
 * getGraphics() can be called multiple times safely, and will always return
 * the same graphics object.
 */
public class RefImage {

    /** a strong reference to the graphics object */
	private Bitmap bi;
    private Canvas g;

    /** Creates a new instance of RefImage */
    public RefImage(int width, int height, Config config) {
    	bi = Bitmap.createBitmap(width, height, config);
    }

    /** 
     * Create a graphics object only if it is currently null, otherwise
     * return the existing graphics object.
     */
    public Canvas createGraphics() {
        if (g == null) {
            g = new Canvas(bi);
        }

        return g;
    }
}
