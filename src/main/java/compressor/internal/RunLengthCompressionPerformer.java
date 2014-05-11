package compressor.internal;

import java.nio.ByteBuffer;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class RunLengthCompressionPerformer {

  public byte[] compressImage(int[] QDCT) {
    int i = 0;
    int temp;
    int runCounter = 0;
    int imageLength = QDCT.length;

    ByteBuffer buffer = ByteBuffer.allocate(imageLength);

    while ((i < imageLength)) {
      temp = QDCT[i];

      while ((i < imageLength) && (temp == QDCT[i])) {
        runCounter++;
        i++;
      }

      if (runCounter > 4) {
        buffer.put((byte) 255);
        buffer.put((byte) temp);
        buffer.put((byte) runCounter);
      } else {
        for (int k = 0; k < runCounter; k++) {
          buffer.put((byte) temp);
        }
      }

      runCounter = 0;
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
    int temp = 0;

    ByteBuffer buffer = ByteBuffer.wrap(compressedImag);
    int pixel[] = new int[imageLength];

    while (i < compressedImag.length) {
      temp = buffer.get();

      if (k < imageLength) {
        if (temp == 255) {
          i++;
          int value = buffer.get();
          i++;
          int length = buffer.get();

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
