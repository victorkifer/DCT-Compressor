package compressor.internal;

import junit.framework.TestCase;

public class ZigZagMatrixTest extends TestCase {

  public void testGetZigZagMatrix() throws Exception {
    final int size = 2;

    int[][] correctZigZag = new int[][] {
        {0, 0},
        {0, 1},
        {1, 0},
        {1, 1}
    };

    int[][] zigzag = ZigZagMatrix.getZigZagMatrix(size);

    for(int i = 0; i < size*size; i++) {
      assertEquals(correctZigZag[i][0], zigzag[i][0]);
      assertEquals(correctZigZag[i][1], zigzag[i][1]);
    }
  }
}