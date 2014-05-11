package compressor.internal;

import junit.framework.TestCase;

public class QuantizationPerformerTest extends TestCase {

  QuantizationPerformer performer;

  public void setUp() throws Exception {
    super.setUp();
    performer = new QuantizationPerformer(10);
  }

  public void testGetQuality() throws Exception {
    assertEquals(10, performer.getQuality());
  }

  public void testGetQuantumMatrix() throws Exception {
    performer.reset(2);

    int[][] correct = new int[][] {
        { 3, 5, 7, 9, 11, 13, 15, 17 },
        { 5, 7, 9, 11, 13, 15, 17, 19 },
        { 7, 9, 11, 13, 15, 17, 19, 21 },
        { 9, 11, 13, 15, 17, 19, 21, 23 },
        { 11, 13, 15, 17, 19, 21, 23, 25 },
        { 13, 15, 17, 19, 21, 23, 25, 27 },
        { 15, 17, 19, 21, 23, 25, 27, 29 },
        { 17, 19, 21, 23, 25, 27, 29, 31 }
    };

    int[][] result = performer.getQuantumMatrix();

    for (int i = 0; i < Config.getBlockSize(); i++) {
      for (int j = 0; j < Config.getBlockSize(); j++) {
        assertEquals(correct[i][j], result[i][j]);
      }
    }

  }

  public void testReset() throws Exception {
    performer.reset(50);

    assertEquals(50, performer.getQuality());
  }

  public void testQuantizeDCT() throws Exception {
    performer.reset(2);

    int[][] input = {
        { 92, 3, -9, -7, 3, -1, 0, 2 },
        { -39, -58, 12, -17, -2, 2, 4, 2 },
        { -84, 62, 1, -18, 3, 4, -5, 5 },
        { -52, -36, -10, 14, -10, 4, -2, 0 },
        { -86, -40, -49, -7, 17, -6, -2, 5 },
        { -62, 65, -12, -2, 3, -8, -2, 0 },
        { -17, 14, -36, 17, -11, 3, 3, -1 },
        { -54, 32, -9, -9, 22, 0, 1, 3 }
    };


  }

  public void testDequantizeDCT() throws Exception {

  }
}