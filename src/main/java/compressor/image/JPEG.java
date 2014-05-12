package compressor.image;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;
import utils.Log;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JPEG extends BaseImage {
  int[][] redMask;
  int[][] greenMask;
  int[][] blueMask;

  public static JPEG fromFile(String filename) throws IOException {
    Log.i("Loading jpeg image...");
    //URL url = new File(filename).toURI().toURL();
    //BufferedImage image = ImageIO.read(url);
    ImageData image = new Image(Display.getDefault(), filename).getImageData();

    return fromImageData(image);
  }

  public static JPEG fromImageData(ImageData image) {
    Log.i("Loading jpeg image...");
    JPEG cImage = new JPEG();

    cImage.width = image.width;
    cImage.height = image.height;

    cImage.normalize();

    int[][] rgb = new int[cImage.height][cImage.width];

    for (int i = 0; i < cImage.height; i++) {
      for (int j = 0; j < cImage.width; j++) {
        rgb[i][j] = image.getPixel(j, i);
      }
    }

    cImage.redMask = cImage.getMaskedArray(rgb, 0x00ff0000, 16);
    cImage.greenMask = cImage.getMaskedArray(rgb, 0x0000ff00, 8);
    cImage.blueMask = cImage.getMaskedArray(rgb, 0x000000ff, 0);

    Log.i("Done");

    return cImage;
  }



  public static class Builder {
    JPEG cImage;

    public Builder() {
      cImage = new JPEG();
    }

    public void originalWidth(int width) {
      cImage.width = width;
    }

    public void originalHeight(int height) {
      cImage.height = height;
    }

    public void redMask(int[][] redMask) {
      cImage.redMask = redMask;
    }

    public void greenMask(int[][] greenMask) {
      cImage.greenMask = greenMask;
    }

    public void blueMask(int[][] blueMask) {
      cImage.blueMask = blueMask;
    }

    public JPEG build() {
      cImage.normalize();
      return cImage;
    }
  }

  private JPEG() { }

  private int[][] getMaskedArray(int[][] image, int mask, int offset)
  {
    int r[][] = new int[normalizedHeight][normalizedWidth];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int value = (image[i][j] & mask) >> offset;
        r[i][j] = value;
      }
    }

    for (int i = height; i < normalizedHeight; i++) {
      for (int j = 0; j < normalizedWidth; j++) {
        r[i][j] = 0;
      }
    }

    for (int j = width; j < normalizedWidth; j++) {
      for (int i = 0; i < normalizedHeight; i++) {
        r[i][j] = 0;
      }
    }

    return r;
  }

  public int[][] getChannel(Channel channel) {
    switch (channel) {
      case Red:
        return redMask;
      case Green:
        return greenMask;
      case Blue:
        return blueMask;
      default:
        return null;
    }
  }

  @Override
  public void toFile(String filename) throws IOException {
    Log.i("Saving jpeg image");
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = ((redMask[i][j] & 0xff) << 16) +
            ((greenMask[i][j] & 0xff) << 8) +
            (blueMask[i][j] & 0xff);
        image.setRGB(j, i, rgb);
      }
    }

    File file = new File(filename);

    ImageOutputStream ios =  ImageIO.createImageOutputStream(file);
    ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
    ImageWriteParam param = writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(1.0F);
    writer.setOutput(ios);
    writer.write(null, new IIOImage(image, null, null), param);
    writer.dispose();

    Log.i("Done");
  }

  public ImageData getImageData() {
    PaletteData PALETTE_DATA = new PaletteData(0xFF0000, 0xFF00, 0xFF);
    ImageData swtImageData = new ImageData(width, height, 24, PALETTE_DATA);
    byte[] data = swtImageData.data;

    for (int i = 0; i < height; i++) {
      int idx = i * swtImageData.bytesPerLine;
      for (int j = 0; j < width; j++) {
        data[idx++] = (byte) redMask[i][j];
        data[idx++] = (byte) greenMask[i][j];
        data[idx++] = (byte) blueMask[i][j];
      }
    }

    return swtImageData;
  }

}
