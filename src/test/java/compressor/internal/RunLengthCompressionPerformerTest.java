package compressor.internal;

import junit.framework.TestCase;

public class RunLengthCompressionPerformerTest extends TestCase {

  RunLengthCompressor performer;

  byte MARKER;

  public void setUp() throws Exception {
    super.setUp();
    performer = new RunLengthCompressor();
    MARKER = RunLengthCompressor.MARKER;
  }

  public void testCompressImage() throws Exception {
    int[] input = {
        12,
        11, 11,
        10, 10, 10,
        9, 9, 9, 9,
        8, 8, 8, 8, 8,
        7, 7, 7, 7, 7, 7,
        6, 6, 6, 6, 6, 6, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        4, 4, 4, 4, 4, 4, 4, 4, 4,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    byte[] correct = {
        12,
        11, 11,
        10, 10, 10,
        9, 9, 9, 9,
        //MARKER, 9, 4,
        MARKER, 8, 0, 5,
        MARKER, 7, 0, 6,
        MARKER, 6, 0, 7,
        MARKER, 5, 0, 8,
        MARKER, 4, 0, 9,
        MARKER, 3, 0, 10,
        MARKER, 2, 0, 11,
        MARKER, 1, 0, 12,
        MARKER, 0, 0, 13
    };

    byte[] result = performer.compressImage(input);

    for (int i = 0; i < result.length; i++) {
      assertEquals(correct[i], result[i]);
    }
  }

  public void testDecompressImage() throws Exception {
    byte[] input = {
        12,
        11, 11,
        10, 10, 10,
        9, 9, 9, 9,
        //MARKER, 9, 4,
        MARKER, 8, 0, 5,
        MARKER, 7, 0, 6,
        MARKER, 6, 0, 7,
        MARKER, 5, 0, 8,
        MARKER, 4, 0, 9,
        MARKER, 3, 0, 10,
        MARKER, 2, 0, 11,
        MARKER, 1, 0, 12,
        MARKER, 0, 0, 13
    };

    int[] correct = {
        12,
        11, 11,
        10, 10, 10,
        9, 9, 9, 9,
        8, 8, 8, 8, 8,
        7, 7, 7, 7, 7, 7,
        6, 6, 6, 6, 6, 6, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        4, 4, 4, 4, 4, 4, 4, 4, 4,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    int[] result = performer.decompressImage(input, correct.length);

    for (int i = 0; i < result.length; i++) {
      assertEquals(correct[i], result[i]);
    }
  }
}