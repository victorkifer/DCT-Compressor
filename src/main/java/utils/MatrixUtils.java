package utils;

import java.io.*;

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

    double [][] tMatrix = new double[width][height];

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

  public static int[] toVector(int[][] matrix, int width, int height) {
    int[] vector = new int[height*width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        vector[i*width+j] = matrix[i][j];
      }
    }

    return vector;
  }

  public static int[][] fromVector(int[] vector) {
    final int height = (int)Math.sqrt(vector.length);
    final int width = height;

    int[][] matrix = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        matrix[i][j] = vector[i*height+j];
      }
    }

    return matrix;
  }

  public static int[][] fromVector(final int[] vector, final int width, final int height) {
    int[][] matrix = new int[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        matrix[i][j] = vector[i*width+j];
      }
    }

    return matrix;
  }

  public static void printChunk(int[][] matrix, int width, int height) {
    System.out.println("-------------------------------------------------------------");
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println("-------------------------------------------------------------");
  }

  public static int[][] getChunk(int[][] matrix, int block_size, int xOffset, int yOffset) {
    final int height = matrix.length;
    final int width = matrix[0].length;

    int offsetX = block_size*xOffset;
    int offsetY = block_size*yOffset;

    int offsetX2 = block_size*(xOffset+1);
    int offsetY2 = block_size*(yOffset+1);

    if(offsetX2 > width)
      return null;
    if(offsetY2 > height)
      return null;

    int[][] chunk = new int[block_size][block_size];

    for (int i = offsetY; i < offsetY2; i++) {
      for (int j = offsetX; j < offsetX2; j++) {
        chunk[i-offsetY][j-offsetX] = matrix[i][j];
      }
    }

    return chunk;
  }

  public static void setChunk(int[][] matrix, int[][] chunk, int xOffset, int yOffset) {
    final int height = matrix.length;
    final int width = matrix[0].length;

    int block_size = chunk.length;

    int offsetX = block_size*xOffset;
    int offsetY = block_size*yOffset;

    int offsetX2 = block_size*(xOffset+1);
    int offsetY2 = block_size*(yOffset+1);

    if(offsetX2 > width)
      return;
    if(offsetY2 > height)
      return;

    for (int i = offsetY; i < offsetY2; i++) {
      for (int j = offsetX; j < offsetX2; j++) {
        matrix[i][j] = chunk[i-offsetY][j-offsetX];
      }
    }
  }

  public static void toFile(int[][] matrix) {
    File file = new File("matrices.txt");
    try {
      OutputStream os = new FileOutputStream(file, true);
      PrintWriter out = new PrintWriter(os);

      for (int[] row : matrix) {
        for (int el : row) {
          out.print(el + " ");
        }
        out.println();
      }
      out.println("-----------------------------------------------------------------");

      out.flush();
      out.close();
      os.flush();
      os.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
