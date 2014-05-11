package compressor.internal;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class QuantizationPerformer {
  private int mQuality = 80;

  private int zigZag[][];
  private int quantum[][];

  public QuantizationPerformer(int quality) {
    mQuality = quality;

    initMatrices();
  }

  public int getQuality() {
    return mQuality;
  }

  private void initMatrices() {
    quantum = new int[Config.getBlockSize()][Config.getBlockSize()];

    initZigZagMatrix();
    initQuantumMatrix();
  }

  private void initZigZagMatrix() {
    zigZag = ZigZagMatrix.getZigZagMatrix(Config.getBlockSize());
  }

  private void initQuantumMatrix() {
    for (int i = 0; i < Config.getBlockSize(); i++) {
      for (int j = 0; j < Config.getBlockSize(); j++) {
        quantum[i][j] = (i+j+1) * mQuality + 1;
      }
    }
  }

  public int[][] getQuantumMatrix() {
    return quantum;
  }

  public void reset(int quality) {
    mQuality = quality;

    initQuantumMatrix();
  }

  public int[][] quantizeDCT(final int dct[][], boolean useZigZag) {
    int quantizedDCT[][] = new int[Config.getBlockSize()][Config.getBlockSize()];

    double result;

    if (useZigZag) {
      for (int i = 0; i < (Config.getBlockSize() * Config.getBlockSize()); i++) {
        int row = zigZag[i][0];
        int col = zigZag[i][1];

        result = (dct[row][col] / quantum[row][col]);
        quantizedDCT[row][col] = (int) Math.round(result);
      }
    } else {
      for (int i = 0; i < Config.getBlockSize(); i++) {
        for (int j = 0; j < Config.getBlockSize(); j++) {
          result = dct[i][j] / quantum[i][j];
          quantizedDCT[i][j] = (int) Math.round(result);
        }
      }
    }

    return quantizedDCT;
  }


  public int[][] dequantizeDCT(int[][] quantizedDCT, boolean useZigZag) {
    int dct[][] = new int[Config.getBlockSize()][Config.getBlockSize()];

    double result;

    if (useZigZag) {
      for (int i = 0; i < (Config.getBlockSize() * Config.getBlockSize()); i++) {
        int row = zigZag[i][0];
        int col = zigZag[i][1];

        result = quantizedDCT[row][col] * quantum[row][col];
        dct[row][col] = (int) Math.round(result);
      }
    } else {
      for (int i = 0; i < Config.getBlockSize(); i++) {
        for (int j = 0; j < Config.getBlockSize(); j++) {
          result = quantizedDCT[i][j] * quantum[i][j];
          dct[i][j] = (int) Math.round(result);
        }
      }
    }

    return dct;
  }

}
