package utils;

import junit.framework.TestCase;

public class MatrixUtilsTest extends TestCase {

  public void testTransposeMatrix() throws Exception {
    double[][] input = {
        { 1, 2, 3 },
        { 4, 5, 6 },
        { 7, 8, 9 },
        { 10, 11, 12 }
    };

    double[][] correct = {
        { 1, 4, 7, 10 },
        { 2, 5, 8, 11 },
        { 3, 6, 9, 12 }
    };

    double[][] result = MatrixUtils.transposeMatrix(input);

    for (int i = 0; i < correct.length; i++) {
      for (int j = 0; j < correct[0].length; j++) {
        assertEquals(correct[i][j], result[i][j]);
      }
    }
  }

  public void testToVector() throws Exception {
    int[][] input = {
        { 1, 2, 3 },
        { 4, 5, 6 },
        { 7, 8, 9 },
        { 10, 11, 12 }
    };

    int[] correct = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

    int[] result = MatrixUtils.toVector(input);

    for (int j = 0; j < correct.length; j++) {
      assertEquals(correct[j], result[j]);
    }
  }

  public void testToVector1() throws Exception {
    int[][] input = {
        { 1, 2, 3 },
        { 4, 5, 6 },
        { 7, 8, 9 },
        { 10, 11, 12 }
    };

    int[] correct = { 1, 2, 4, 5, 7, 8 };

    int[] result = MatrixUtils.toVector(input, 2, 3);

    for (int j = 0; j < correct.length; j++) {
      assertEquals(correct[j], result[j]);
    }
  }

  public void testFromVector() throws Exception {
    int[] input = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

    int[][] correct = {
        { 1, 2, 3, 4 },
        { 5, 6, 7, 8 },
        { 9, 10, 11, 12 },
        { 13, 14, 15, 16 }
    };

    int[][] result = MatrixUtils.fromVector(input);

    for (int i = 0; i < correct.length; i++) {
      for (int j = 0; j < correct[0].length; j++) {
        assertEquals(correct[i][j], result[i][j]);
      }
    }
  }

  public void testFromVector1() throws Exception {
    int[] input = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

    int[][] correct = {
        { 1, 2, 3 },
        { 4, 5, 6 },
        { 7, 8, 9 },
        { 10, 11, 12 }
    };

    int[][] result = MatrixUtils.fromVector(input, 3, 4);

    for (int i = 0; i < correct.length; i++) {
      for (int j = 0; j < correct[0].length; j++) {
        assertEquals(correct[i][j], result[i][j]);
      }
    }
  }

  public void testGetChunk() throws Exception {
    int[][] input = {
        { 1, 2, 3, 4 },
        { 5, 6, 7, 8 },
        { 9, 10, 11, 12 },
        { 13, 14, 15, 16 }
    };

    int[][] correct = {
        { 3, 4 },
        { 7, 8 }
    };

    int[][] result = MatrixUtils.getChunk(input, 2, 1, 0);

    for (int i = 0; i < correct.length; i++) {
      for (int j = 0; j < correct[0].length; j++) {
        assertEquals(correct[i][j], result[i][j]);
      }
    }
  }

  public void testSetChunk() throws Exception {
    int[][] input = {
        { 1, 2, 3, 4 },
        { 5, 6, 7, 8 },
        { 9, 10, 11, 12 },
        { 13, 14, 15, 16 }
    };

    int[][] input2 = {
        { 17, 18 },
        { 19, 20 }
    };

    int[][] correct = {
        { 1, 2, 3, 4 },
        { 5, 6, 7, 8 },
        { 17, 18, 11, 12 },
        { 19, 20, 15, 16 }
    };

    MatrixUtils.setChunk(input, input2, 0, 1);

    for (int i = 0; i < correct.length; i++) {
      for (int j = 0; j < correct[0].length; j++) {
        assertEquals(correct[i][j], input[i][j]);
      }
    }
  }
}