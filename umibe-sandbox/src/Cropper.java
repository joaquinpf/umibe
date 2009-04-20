import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Cropper {
    /**
     * Perform crop
     * <a href="http://forum.java.sun.com/thread.jspa?threadID=627125&messageID=3587532">Reference</a>
     */
    public void crop() {
        Rectangle r = ImageCropper.this.cropPanel.getClip();
        if (!this.isClipInsideImage(r)) /* AVOID RasterFormatException */ {
            return;
        }
        // offset clip from view to raster model
        int x = (ImageCropper.this.cropPanel.getWidth() -
                ImageCropper.this.cropPanel.getImage().getWidth()) / 2;
        int y = (ImageCropper.this.cropPanel.getHeight() -
                ImageCropper.this.cropPanel.getImage().getHeight()) / 2;
        BufferedImage clippedImage =
                ImageCropper.this.cropPanel.getImage().getSubimage(
                (int)r.getX() - x,
                (int)r.getY() - y,
                (int)r.getWidth(),
                (int)r.getHeight());
        ImageCropper.this.cropPanel.setImage(clippedImage);
        ImageCropper.this.cropPanel.setup();
        ImageCropper.this.validate();
    }
    
    /** 
     * Determine if the {@link java.awt.image.BufferedImage} has a legitimate {@link java.awt.Rectangle} clip
     * To avoid {@link java.awt.image.RasterFormatException}
     * @param r {@link java.awt.Rectangle}
     * @return {@link java.lang.Boolean}
     * <a href="http://forum.java.sun.com/thread.jspa?threadID=627125&messageID=3587532">Reference</a>
     */
    private boolean isClipInsideImage(Rectangle r) {
        int w = ImageCropper.this.cropPanel.getWidth();
        int h = ImageCropper.this.cropPanel.getHeight();
        int imageWidth = ImageCropper.this.cropPanel.getImage().getWidth();
        int imageHeight = ImageCropper.this.cropPanel.getImage().getHeight();
        int x = (w - imageWidth) / 2;
        int y = (h - imageHeight) / 2;
        if ((int)r.getX() >= x &&
                (int)r.getX() + (int)r.getWidth() <= x + imageWidth &&
                (int)r.getY() >= y &&
                (int)r.getY() + (int)r.getHeight() <= y + imageHeight) {
            return true;
        }
        return false;
        
        
    }
}
