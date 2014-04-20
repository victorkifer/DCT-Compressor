package utils;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class MatrixUtils {

  public static void displayMatrix(int[][] matrix) {
    for (int[] row : matrix) {
      for(int el : row) {
        System.out.print(el + " ");
      }
      System.out.println();
    }
  }

  public static void displayMatrix(double[][] matrix, int width, int height) {
    for (double[] row : matrix) {
      for(double el : row) {
        System.out.print(el + " ");
      }
      System.out.println();
    }
  }

  public static double[][] transposeMatrix(double[][] matrix) {
    final int height = matrix.length;
    final int width = matrix[0].length;

    double [][] tMatrix = new double[height][width];

    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        tMatrix[j][i] = matrix[i][j];
      }
    }

    return tMatrix;
  }

  public static int[] toVector(int[][] matrix) {
    final int height = matrix.length;
    final int width = matrix[0].length;

    int[] vector = new int[height*width];

    int pos = 0;
    for (int[] row : matrix) {
      for (int el : row) {
        vector[pos] = el;
        pos++;
      }
    }

    return vector;
  }

}
