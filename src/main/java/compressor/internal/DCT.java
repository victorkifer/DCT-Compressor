package compressor.internal;

import utils.MatrixUtils;

public class DCT {

  private double cos[][];
  private double cosT[][];

  public DCT() {
    initMatrices();
  }

  private void initMatrices() {
    initCosineMatrix();
    initTransposedCosineMatrix();
  }

  private void initCosineMatrix() {
    cos = new double[Config.getBlockSize()][Config.getBlockSize()];

    double v = 1.0 / Math.sqrt((double)Config.getBlockSize());
    for(int j = 0; j < Config.getBlockSize(); j++) {
      cos[0][j] = v;
    }

    v = Math.sqrt(2.0 / (1.0*Config.getBlockSize()));
    for (int i = 1; i < Config.getBlockSize(); i++) {
      for(int j = 0; j < Config.getBlockSize(); j++) {
        cos[i][j] = v * Math.cos((2.0*j+1.0)*i*Math.PI / (2.0*Config.getBlockSize()));
      }
    }
  }

  private void initTransposedCosineMatrix() {
    cosT = MatrixUtils.transposeMatrix(cos);
  }

  public int[][] forwardDCT(final int input[][]) {
    int dct[][] = new int[Config.getBlockSize()][Config.getBlockSize()];

    double tmpMtx[][] = new double[Config.getBlockSize()][Config.getBlockSize()];
    for(int i = 0; i < Config.getBlockSize(); i++) {
      for(int j = 0; j < Config.getBlockSize(); j++) {
        tmpMtx[i][j] = 0.0;
        for(int k = 0; k < Config.getBlockSize(); k++) {
          tmpMtx[i][j] += (int)((input[i][k]-128) * cosT[k][j]);
        }
      }
    }

    for(int i = 0; i < Config.getBlockSize(); i++) {
      for(int j = 0; j < Config.getBlockSize(); j++) {
        double tmp = 0.0;
        for(int k = 0; k < Config.getBlockSize(); k++) {
          tmp += cos[i][k] * tmpMtx[k][j];
        }
        dct[i][j] = (int) Math.round(tmp);
      }
    }

    return dct;
  }

  public int[][] inverseDCT(int input[][]) {
    int inverseDCT[][] = new int[Config.getBlockSize()][Config.getBlockSize()];

    double tmpMtx[][] = new double[Config.getBlockSize()][Config.getBlockSize()];
    for (int i = 0; i < Config.getBlockSize(); i++) {
      for (int j = 0; j < Config.getBlockSize(); j++) {
        tmpMtx[i][j] = 0.0;
        for (int k = 0; k < Config.getBlockSize(); k++) {
          tmpMtx[i][j] += input[i][k] * cos[k][j];
        }
      }
    }

    for (int i = 0; i < Config.getBlockSize(); i++) {
      for (int j = 0; j < Config.getBlockSize(); j++) {
        double tmp = 128.0;

        for (int k = 0; k < Config.getBlockSize(); k++) {
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

}
