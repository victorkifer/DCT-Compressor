package compressor.image;

import utils.Log;
import utils.MatrixUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class CJPEG extends BaseImage {

  byte[] cRedMask;
  byte[] cGreenMask;
  byte[] cBlueMask;

  public static CJPEG fromFile(String filename) throws IOException {
    File file = new File(filename);
    CJPEG cImage = new CJPEG();

    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      Log.i("Loading compressed image");
      // reading height
      cImage.height = getInt(fileInputStream);

      // reading width
      cImage.width = getInt(fileInputStream);

      // reading compressed red mask
      int compressedMaskLen = getInt(fileInputStream);
      cImage.cRedMask = new byte[compressedMaskLen];
      fileInputStream.read(cImage.cRedMask);

      // reading compressed green mask
      compressedMaskLen = getInt(fileInputStream);
      cImage.cGreenMask = new byte[compressedMaskLen];
      fileInputStream.read(cImage.cGreenMask);

      // reading compressed blue mask
      compressedMaskLen = getInt(fileInputStream);
      cImage.cBlueMask = new byte[compressedMaskLen];
      fileInputStream.read(cImage.cBlueMask);

      fileInputStream.close();

      cImage.normalize();
    }

    MatrixUtils.printVector(cImage.cRedMask, 20);
    MatrixUtils.printVector(cImage.cGreenMask, 20);
    MatrixUtils.printVector(cImage.cBlueMask, 20);

    Log.i("Done");

    return cImage;
  }

  private static int getInt(FileInputStream fis) throws IOException {
    byte[] intBytes = new byte[4];
    ByteBuffer bb = ByteBuffer.allocate(4);
    int result;

    fis.read(intBytes);
    bb.put(intBytes);
    bb.flip();
    result = bb.getInt();
    bb.clear();

    return result;
  }

  public static class Builder {
    CJPEG cImage;

    public Builder() {
      cImage = new CJPEG();
    }

    public void width(int width) {
      cImage.width = width;
    }

    public void height(int height) {
      cImage.height = height;
    }

    public void redMask(byte[] redMask) {
      cImage.cRedMask = redMask;
    }

    public void greenMask(byte[] greenMask) {
      cImage.cGreenMask = greenMask;
    }

    public void blueMask(byte[] blueMask) {
      cImage.cBlueMask = blueMask;
    }

    public CJPEG build() {
      cImage.normalize();
      return cImage;
    }
  }

  public byte[] getCompressedChannel(Channel channel) {
    switch (channel) {
      case Red:
        return cRedMask;
      case Green:
        return cGreenMask;
      case Blue:
        return cBlueMask;
      default:
        return null;
    }
  }

  private CJPEG() { }

  @Override
  public void toFile(String filename) throws IOException {
    Log.i("Saving compressed image");

    ByteBuffer buffer = ByteBuffer.allocate(4*5+cRedMask.length+cGreenMask.length+cBlueMask.length);
    buffer.putInt(height);
    buffer.putInt(width);

    buffer.putInt(cRedMask.length);
    for (byte i : cRedMask) {
      buffer.put(i);
    }

    buffer.putInt(cGreenMask.length);
    for (byte i : cGreenMask) {
      buffer.put(i);
    }

    buffer.putInt(cBlueMask.length);
    for (byte i : cBlueMask) {
      buffer.put(i);
    }

    MatrixUtils.printVector(cRedMask, 20);
    MatrixUtils.printVector(cGreenMask, 20);
    MatrixUtils.printVector(cBlueMask, 20);

    buffer.flip();
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    try (FileOutputStream stream = new FileOutputStream(filename)) {
      stream.write(bytes);

      stream.close();
    }

    Log.i("Done");
  }
}
