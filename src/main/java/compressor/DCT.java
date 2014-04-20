package compressor;

import utils.MatrixUtils;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class DCT {
  private final int BLOCK_SIZE = 8;

  private int mQuality = 50;

  private int zigZag[][];
  private double cos[][];
  private double cosT[][];
  private int quantum[][];

  private int imageWidth;
  private int imageHeight;

  public DCT(int quality) {
    mQuality = quality;

    initMatrices();
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

    double v = 1.0 / Math.sqrt(BLOCK_SIZE);
    for(int j = 0; j < BLOCK_SIZE; j++) {
      cos[0][j] = v;
    }

    v = Math.sqrt(2.0 / (1.0*BLOCK_SIZE));
    for (int i = 1; i < BLOCK_SIZE; i++) {
      for(int j = 0; j < BLOCK_SIZE; j++) {
        cos[i][j] = v * Math.cos((2*j+1)*i*Math.PI / (2.0*BLOCK_SIZE));
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
          tmpMtx[i][j] += input[i][k] * cosT[k][j];
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
}
