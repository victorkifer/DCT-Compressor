package compressor.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class JPEG extends BaseImage {
  int[][] redMask;
  int[][] greenMask;
  int[][] blueMask;

  public static JPEG fromFile(String filename) throws IOException {
    URL url = new File(filename).toURI().toURL();
    BufferedImage image = ImageIO.read(url);

    JPEG cImage = new JPEG();

    cImage.width = image.getWidth();
    cImage.height = image.getHeight();

    cImage.normalize();

    int[][] rgb = new int[cImage.height][cImage.width];

    for (int i = 0; i < cImage.height; i++) {
      for (int j = 0; j < cImage.width; j++) {
        rgb[i][j] = image.getRGB(j, i);
      }
    }

    cImage.redMask = cImage.getMaskedArray(rgb, 0x00ff0000, 16);
    cImage.greenMask = cImage.getMaskedArray(rgb, 0x0000ff00, 8);
    cImage.blueMask = cImage.getMaskedArray(rgb, 0x000000ff, 0);

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
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = ((redMask[i][j] & 0xff) << 16) +
            ((greenMask[i][j] & 0xff) << 8) +
            (blueMask[i][j] & 0xff);
        image.setRGB(j, i, rgb);
      }
    }

    ImageIO.write(image, "jpg", new File(filename));
  }
}
