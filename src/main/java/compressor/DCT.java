package compressor;

import compressor.internal.ZigZagMatrix;
import utils.MatrixUtils;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class DCT {
  private static final int BLOCK_SIZE = 8;

  private int mQuality = 80;

  private int zigZag[][];
  private double cos[][];
  private double cosT[][];
  private int quantum[][];

  private int imageWidth = BLOCK_SIZE;
  private int imageHeight = BLOCK_SIZE;

  public static int getBlockSize() {
    return BLOCK_SIZE;
  }

  public DCT(int quality) {
    mQuality = quality;

    initMatrices();
  }

  public void setImageWidth(int width) {
    imageWidth = width;
  }

  public void setImageHeight(int height) {
    imageHeight = height;
  }

  private void initMatrices() {
    initZigZagMatrix();
    initCosineMatrix();
    initTransposedCosineMatrix();
    initQuantumMatrix();
  }

  private void initZigZagMatrix() {
    zigZag = ZigZagMatrix.getZigZagMatrix(BLOCK_SIZE);
  }

  private void initCosineMatrix() {
    cos = new double[BLOCK_SIZE][BLOCK_SIZE];

    double v = 1.0 / Math.sqrt((double)BLOCK_SIZE);
    for(int j = 0; j < BLOCK_SIZE; j++) {
      cos[0][j] = v;
    }

    v = Math.sqrt(2.0 / (1.0*BLOCK_SIZE));
    for (int i = 1; i < BLOCK_SIZE; i++) {
      for(int j = 0; j < BLOCK_SIZE; j++) {
        cos[i][j] = v * Math.cos((2.0*j+1.0)*i*Math.PI / (2.0*BLOCK_SIZE));
      }
    }
  }

  private void initTransposedCosineMatrix() {
    cosT = MatrixUtils.transposeMatrix(cos);
  }

  private void initQuantumMatrix() {
    quantum = new int[BLOCK_SIZE][BLOCK_SIZE];

    for (int i = 0; i < BLOCK_SIZE; i++) {
      for (int j = 0; j < BLOCK_SIZE; j++) {
        quantum[i][j] = (i+j+1) * mQuality + 1;
      }
    }
  }

  public int[][] forwardDCT(final int input[][]) {
    int dct[][] = new int[BLOCK_SIZE][BLOCK_SIZE];

    double tmpMtx[][] = new double[BLOCK_SIZE][BLOCK_SIZE];
    for(int i = 0; i < BLOCK_SIZE; i++) {
      for(int j = 0; j < BLOCK_SIZE; j++) {
        tmpMtx[i][j] = 0.0;
        for(int k = 0; k < BLOCK_SIZE; k++) {
          tmpMtx[i][j] += (int)((input[i][k]-128) * cosT[k][j]);
        }
      }
    }

    for(int i = 0; i < BLOCK_SIZE; i++) {
      for(int j = 0; j < BLOCK_SIZE; j++) {
        double tmp = 0.0;
        for(int k = 0; k < BLOCK_SIZE; k++) {
          tmp += cos[i][k] * tmpMtx[k][j];
        }
        dct[i][j] = (int) Math.round(tmp);
      }
    }

    return dct;
  }

  public int[][] quantizeDCT(final int dct[][], boolean useZigZag) {
    int quantizedDCT[][] = new int[BLOCK_SIZE][BLOCK_SIZE];

    double result;

    if (useZigZag) {
      for (int i = 0; i < (BLOCK_SIZE * BLOCK_SIZE); i++) {
        int row = zigZag[i][0];
        int col = zigZag[i][1];

        result = (dct[row][col] / quantum[row][col]);
        quantizedDCT[row][col] = (int) Math.round(result);
      }
    } else {
      for (int i = 0; i < BLOCK_SIZE; i++) {
        for (int j = 0; j < BLOCK_SIZE; j++) {
          result = dct[i][j] / quantum[i][j];
          quantizedDCT[i][j] = (int) Math.round(result);
        }
      }
    }

    return quantizedDCT;
  }

  public byte[] compressImage(int[] QDCT, boolean log) {
    int i = 0;
    int j = 0;
    int temp = 0;
    int runCounter = 0;
    int imageLength = imageWidth * imageHeight;

    byte pixel[] = new byte[imageLength];

    while ((i < imageLength)) {
      temp = QDCT[i];

      while ((i < imageLength) && (temp == QDCT[i])) {
        runCounter++;
        i++;
      }

      if (runCounter > 4) {
        pixel[j] = (byte)255;
        j++;
        pixel[j] = (byte)temp;
        j++;
        pixel[j] = (byte)runCounter;
        j++;
      } else {
        for (int k = 0; k < runCounter; k++) {
          pixel[j] = (byte)temp;
          j++;
        }
      }

      if (log) {
        System.out.print("." + "\r");
      }

      runCounter = 0;
      i++;
    }

    return pixel;
  }

  public int[][] inverseDCT(int input[][]) {
    int inverseDCT[][] = new int[BLOCK_SIZE][BLOCK_SIZE];

    double tmpMtx[][] = new double[BLOCK_SIZE][BLOCK_SIZE];
    for (int i = 0; i < BLOCK_SIZE; i++) {
      for (int j = 0; j < BLOCK_SIZE; j++) {
        tmpMtx[i][j] = 0.0;
        for (int k = 0; k < BLOCK_SIZE; k++) {
          tmpMtx[i][j] += input[i][k] * cos[k][j];
        }
      }
    }

    for (int i = 0; i < BLOCK_SIZE; i++) {
      for (int j = 0; j < BLOCK_SIZE; j++) {
        double tmp = 128.0;

        for (int k = 0; k < BLOCK_SIZE; k++) {
          tmp += cosT[i][k] * tmpMtx[k][j];
        }

        if (tmp < 0) {
          inverseDCT[i][j] = 0;
        } else if (tmp > 255) {
          inverseDCT[i][j] = 255;
        } else {
          inverseDCT[i][j] = (int) Math.round(tmp);
        }
      }
    }

    return inverseDCT;
  }

  public int[][] dequantizeDCT(int[][] quantizedDCT, boolean useZigZag) {
    int dct[][] = new int[BLOCK_SIZE][BLOCK_SIZE];

    double result;

    if (useZigZag) {
      for (int i = 0; i < (BLOCK_SIZE * BLOCK_SIZE); i++) {
        int row = zigZag[i][0];
        int col = zigZag[i][1];

        result = quantizedDCT[row][col] * quantum[row][col];
        dct[row][col] = (int) Math.round(result);
      }
    } else {
      for (int i = 0; i < BLOCK_SIZE; i++) {
        for (int j = 0; j < BLOCK_SIZE; j++) {
          result = quantizedDCT[i][j] * quantum[i][j];
          dct[i][j] = (int) Math.round(result);
        }
      }
    }

    return dct;
  }

  public int[] decompressImage(int[] DCT, boolean log) {
    int i = 0;
    int j = 0;
    int k = 0;
    int temp = 0;
    int imageLength = imageWidth * imageHeight;
    int pixel[] = new int[imageWidth * imageHeight];

    while (i < imageLength) {
      temp = DCT[i];

      if (k < imageLength) {
        if (temp == 255) {
          i++;
          int value = DCT[i];
          i++;
          int length = DCT[i];

          for (j = 0; j < length; j++) {
            pixel[k] = value;
            k++;
          }
        } else {
          pixel[k] = temp;
          k++;
        }
      }
      if (log) {
        System.out.print("..");
      }

      i++;
    }

    for (int a = 0; a < 80; a++) {
      System.out.print(pixel[a] + " ");
    }
    System.out.println();
    for (int a = 0; a < 80; a++) {
      System.out.print(DCT[a] + " ");
    }

    return pixel;
  }
}
