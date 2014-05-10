package compressor.image;

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

  int[] cRedMask;
  int[] cGreenMask;
  int[] cBlueMask;

  public static CJPEG fromFile(String filename) throws IOException {
    FileInputStream fileInputStream;

    File file = new File(filename);
    CJPEG cImage = new CJPEG();

    byte[] intBytes = new byte[4];
    ByteBuffer bb = ByteBuffer.allocate(4);

    //convert file into array of bytes
    fileInputStream = new FileInputStream(file);

    // reading header

    // reading height
    fileInputStream.read(intBytes);
    bb.put(intBytes);
    cImage.height = bb.getInt();
    bb.clear();

    // reading width
    fileInputStream.read(intBytes);
    bb.put(intBytes);
    cImage.width = bb.getInt();
    bb.clear();

    int compressedMaskLen;

    // reading compressed red mask
    fileInputStream.read(intBytes);
    bb.put(intBytes);
    compressedMaskLen = bb.getInt();
    bb.clear();

    cImage.cRedMask = new int[compressedMaskLen];
    for (int i = 0; i < compressedMaskLen; i++) {
      fileInputStream.read(intBytes);
      bb.put(intBytes);
      cImage.cRedMask[i] = bb.getInt();
      bb.clear();
    }

    // reading compressed green mask
    fileInputStream.read(intBytes);
    bb.put(intBytes);
    compressedMaskLen = bb.getInt();
    bb.clear();

    cImage.cGreenMask = new int[compressedMaskLen];
    for (int i = 0; i < compressedMaskLen; i++) {
      fileInputStream.read(intBytes);
      bb.put(intBytes);
      cImage.cGreenMask[i] = bb.getInt();
      bb.clear();
    }

    // reading compressed blue mask
    fileInputStream.read(intBytes);
    bb.put(intBytes);
    compressedMaskLen = bb.getInt();
    bb.clear();

    cImage.cBlueMask = new int[compressedMaskLen];
    for (int i = 0; i < compressedMaskLen; i++) {
      fileInputStream.read(intBytes);
      bb.put(intBytes);
      cImage.cBlueMask[i] = bb.getInt();
      bb.clear();
    }

    fileInputStream.close();

    return cImage;
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

    public void redMask(int[] redMask) {
      cImage.cRedMask = redMask;
    }

    public void greenMask(int[] greenMask) {
      cImage.cGreenMask = greenMask;
    }

    public void blueMask(int[] blueMask) {
      cImage.cBlueMask = blueMask;
    }

    public CJPEG build() {
      cImage.normalize();
      return cImage;
    }
  }

  public int[] getCompressedChannel(Channel channel) {
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
    ByteBuffer bb = ByteBuffer.allocate(4*(5+cRedMask.length+cGreenMask.length+cBlueMask.length));
    bb.putInt(height);
    bb.putInt(width);
    bb.putInt(cRedMask.length);
    bb.putInt(cGreenMask.length);
    bb.putInt(cBlueMask.length);

    for (int i : cRedMask) {
      bb.putInt(i);
    }
    for (int i : cGreenMask) {
      bb.putInt(i);
    }
    for (int i : cBlueMask) {
      bb.putInt(i);
    }

    try (FileOutputStream stream = new FileOutputStream(filename)) {
      stream.write(bb.array());
    }
  }
}
