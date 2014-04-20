import compressor.DCT;
import junit.framework.TestCase;
import org.junit.Before;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class DCTTest extends TestCase {
  private DCT dct;

  @Before
  public void setUp() throws Exception {
    dct = new DCT(50);
  }

  public void testForwardDCT() throws Exception {
    int[][] input = new int[][] {
        { 140, 144, 147, 140, 140, 155, 179, 175 },
        { 144, 152, 140, 147, 140, 148, 167, 179 },
        { 152, 155, 136, 167, 163, 162, 152, 172 },
        { 168, 145, 156, 160, 152, 155, 136, 160 },
        { 162, 148, 156, 148, 140, 136, 147, 162 },
        { 147, 167, 140, 155, 155, 140, 136, 162 },
        { 136, 156, 123, 167, 162, 144, 140, 147 },
        { 148, 155, 136, 155, 152, 147, 147, 136 }
    };

    int[][] correctOutput = new int[][] {
        { 1210, -18, 15, -9, 23, -9, -14, -19 },
        { 21, -34, 26, -9, -11, 11, 14, 7 },
        { -10, -24, -2, 6, -18, 3, -20, -1 },
        { -8, -5, 14, -15, -8, -3, -3, 8 },
        { -3, 10, 8, 1, -11, 18, 18, 15 },
        { 4, -2, -18, 8, 8, -4, 1, -7 },
        { 9, 1, -3, 4, -1, -7, -1, -2 },
        { 0, -8, -2, 2, 1, 4, -6, 0 }
    };

    int[][] output = dct.forwardDCT(input);

    final int size = 8;

    for(int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(correctOutput[i][j], output[i][j]);
      }
    }
  }
}
