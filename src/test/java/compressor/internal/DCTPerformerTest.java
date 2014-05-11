package compressor.internal;

import junit.framework.TestCase;

public class DCTPerformerTest extends TestCase {

  DCTPerformer performer;

  static final int PRECISION = 4;

  public void setUp() throws Exception {
    super.setUp();
    performer = new DCTPerformer();
  }

  public void testForwardDCT() throws Exception {
    int[][] input = {
        { 140,  144,  147,  140,  140,  155,  179,  175 },
        { 144,  152,  140,  147,  140,  148,  167,  179 },
        { 152,  155,  136,  167,  163,  162,  152,  172 },
        { 168,  145,  156,  160,  152,  155,  136,  160 },
        { 162,  148,  156,  148,  140,  136,  147,  162 },
        { 147,  167,  140,  155,  155,  140,  136,  162 },
        { 136,  156,  123,  167,  162,  144,  140,  147 },
        { 148,  155,  136,  155,  152,  147,  147,  136 }
    };

    int[][] correct = {
        { 176,  -19,  13,  -9,   24,  -8, -15,  -19 },
        { 21,   -36,  28,  -9,  -11,  11,  13,   6 },
        { -11,  -24,  -1,   6,  -19,   4, -20,  -1 },
        { -7,    -6,  13, -15,   -9,  -2,  -3,   9 },
        { -3,    10,   8,   2,   -9,  20,  17,  15 },
        {  4,    -1, -19,   9,    8,  -3,  1,   -7 },
        {  8,     2,  -2,   4,    0,  -9, -3,   -2 },
        {  1,    -8,  -3,   2,    2,   5, -6,    0 }
    };

    int[][] result = performer.forwardDCT(input);

    for (int i = 0; i < Config.getBlockSize(); i++) {
      for (int j = 0; j < Config.getBlockSize(); j++) {
        //assertEquals(correct[i][j], result[i][j]);
        assertTrue(Math.abs(correct[i][j] - result[i][j]) < PRECISION);
      }
    }
  }

  public void testInverseDCT() throws Exception {
    int[][] input = {
        { 176,  -19,  13,  -9,   24,  -8, -15,  -19 },
        { 21,   -36,  28,  -9,  -11,  11,  13,   6 },
        { -11,  -24,  -1,   6,  -19,   4, -20,  -1 },
        { -7,    -6,  13, -15,   -9,  -2,  -3,   9 },
        { -3,    10,   8,   2,   -9,  20,  17,  15 },
        {  4,    -1, -19,   9,    8,  -3,  1,   -7 },
        {  8,     2,  -2,   4,    0,  -9, -3,   -2 },
        {  1,    -8,  -3,   2,    2,   5, -6,    0 }
    };

    int[][] correct = {
        { 140,  144,  147,  140,  140,  155,  179,  175 },
        { 144,  152,  140,  147,  140,  148,  167,  179 },
        { 152,  155,  136,  167,  163,  162,  152,  172 },
        { 168,  145,  156,  160,  152,  155,  136,  160 },
        { 162,  148,  156,  148,  140,  136,  147,  162 },
        { 147,  167,  140,  155,  155,  140,  136,  162 },
        { 136,  156,  123,  167,  162,  144,  140,  147 },
        { 148,  155,  136,  155,  152,  147,  147,  136 }
    };

    int[][] result = performer.inverseDCT(input);

    for (int i = 0; i < Config.getBlockSize(); i++) {
      for (int j = 0; j < Config.getBlockSize(); j++) {
        //assertEquals(correct[i][j], result[i][j]);
        assertTrue(Math.abs(correct[i][j]-result[i][j]) < PRECISION);
      }
    }
  }
}