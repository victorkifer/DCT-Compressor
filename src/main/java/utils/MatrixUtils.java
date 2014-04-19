package utils;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class MatrixUtils {

  public static void displayMatrix(int[][] matrix, int width, int height) {
    for (int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }

  public static void displayMatrix(double[][] matrix, int width, int height) {
    for (int i = 0; i < height; i++) {
      for(int j = 0; i < width; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }

}
