package compressor.internal;

import java.nio.ByteBuffer;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class RunLengthCompressionPerformer {

  public static final byte MARKER = -128;

  public byte[] compressImage(int[] QDCT) {
    int i = 0;
    byte temp;
    short runCounter = 1;
    int imageLength = QDCT.length;

    ByteBuffer buffer = ByteBuffer.allocate(imageLength);

    while (i < imageLength) {
      temp = (byte)QDCT[i];
      i++;

      while ((i < imageLength) && (temp == (byte)QDCT[i])) {
        runCounter++;
        i++;
      }

      if(temp == MARKER)
        temp++;

      if (runCounter > 4) {
        buffer.put(MARKER);
        buffer.put(temp);
        buffer.putShort(runCounter);
      } else {
        for (int k = 0; k < runCounter; k++) {
          buffer.put(temp);
        }
      }

      runCounter = 1;
    }

    buffer.flip();
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);

    return bytes;
  }

  public int[] decompressImage(byte[] compressedImag, int imageLength) {
    int i = 0;
    int j = 0;
    int k = 0;
    byte temp = 0;

    ByteBuffer buffer = ByteBuffer.wrap(compressedImag);
    int pixel[] = new int[imageLength];

    while (i < compressedImag.length) {
      temp = buffer.get();

      if (k < imageLength) {
        if (temp == MARKER) {
          i++;
          int value = buffer.get();
          i++;

          short length = buffer.getShort();
          i++;

          for (j = 0; j < length; j++) {
            pixel[k] = value;
            k++;
          }
        } else {
          pixel[k] = temp;
          k++;
        }
      }

      i++;
    }

    return pixel;
  }
}
