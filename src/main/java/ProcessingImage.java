import compressor.DCT;
import compressor.image.Channel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class ProcessingImage {
  private Pixel[][] pixels;

  private boolean hasAlphaChannel = false;

  private int mOriginWidth;
  private int mOriginHeight;

  private int mHeight;
  private int mWidth;

  public static ProcessingImage fromFile(String file) throws IOException {
    URL url = new File(file).toURI().toURL();
    BufferedImage image = ImageIO.read(url);

    return new ProcessingImage(image);
  }

  private ProcessingImage(BufferedImage image) {
    mOriginHeight = image.getHeight();
    mOriginWidth = image.getWidth();

    int size = DCT.getBlockSize();

    mHeight = mOriginHeight;
    mWidth = mOriginWidth;

    if(mOriginHeight % size != 0) {
      mHeight += size - mOriginHeight%size;
    }
    if(mOriginWidth % size != 0) {
      mWidth += size - mOriginWidth%size;
    }

    hasAlphaChannel = image.getAlphaRaster() != null;

    pixels = new Pixel[mHeight][mWidth];

    for (int i = 0; i < mHeight; i++) {
      for (int j = 0; j < mWidth; j++) {
        if(i < mOriginHeight && j < mOriginWidth) {
          pixels[i][j] = new Pixel(image.getRGB(j, i));
        } else {
          pixels[i][j] = new Pixel(0, 0, 0);
        }
      }
    }
  }

  public int getWidth() {
    return mWidth;
  }

  public int getHeight() {
    return mHeight;
  }

  public int[][] getChannel(Channel channel) {
    int[][] matrix = new int[mHeight][mWidth];

    for (int i = 0; i < mHeight; i++) {
      for (int j = 0; j < mWidth; j++) {
        matrix[i][j] = pixels[i][j].getChannel(channel);
      }
    }

    return matrix;
  }

  public void setChannel(Channel channel, int[][] matrix) {
    for (int i = 0; i < mHeight; i++) {
      for (int j = 0; j < mWidth; j++) {
        switch (channel) {
          case Red:
            pixels[i][j].setRed(matrix[i][j]);
            break;
          case Green:
            pixels[i][j].setGreen(matrix[i][j]);
            break;
          case Blue:
            pixels[i][j].setBlue(matrix[i][j]);
            break;
        }
      }
    }
  }

  public BufferedImage toBufferedImage() {
    BufferedImage image;
    if (hasAlphaChannel) {
      image = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_ARGB);
    } else {
      image = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);
    }

    for (int i = 0; i < mHeight; i++) {
      for (int j = 0; j < mWidth; j++) {
        if(hasAlphaChannel) {
          image.setRGB(j, i, pixels[i][j].toARGB());
        } else {
          image.setRGB(j, i, pixels[i][j].toRGB());
        }
      }
    }

    return image;
  }
}
