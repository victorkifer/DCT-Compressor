package compressor.internal;

import java.nio.IntBuffer;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class RunCounterCompressionPerformer {

  public int[] compressImage(int[] QDCT) {
    int i = 0;
    int temp;
    int runCounter = 0;
    int imageLength = QDCT.length;

    IntBuffer ib = IntBuffer.allocate(imageLength);

    while ((i < imageLength)) {
      temp = QDCT[i];

      while ((i < imageLength) && (temp == QDCT[i])) {
        runCounter++;
        i++;
      }

      if (runCounter > 4) {
        ib.put(255);
        ib.put(temp);
        ib.put(runCounter);
      } else {
        for (int k = 0; k < runCounter; k++) {
          ib.put(temp);
        }
      }

      runCounter = 0;
    }

    return ib.array();
  }

  public int[] decompressImage(int[] compressedImag, int imageLength) {
    int i = 0;
    int j = 0;
    int k = 0;
    int temp = 0;
    IntBuffer bb = IntBuffer.wrap(compressedImag);
    int pixel[] = new int[imageLength];

    while (i < imageLength) {
      temp = bb.get();

      if (k < imageLength) {
        if (temp == 255) {
          i++;
          int value = bb.get();
          i++;
          int length = bb.get();

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
