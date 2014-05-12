package compressor.internal;

public class ZigZagMatrix {

  public static int[][] getZigZagMatrix(int a) {
    return getZigZagMatrix(a, a);
  }

  public static int[][] getZigZagMatrix(int width, int height) {
    int[][] zigZag = new int[width*height][2];

    int i = 0;
    int j = 0;
    for (int element = 0; element < width * height; element++) {
      zigZag[element][0] = i;
      zigZag[element][1] = j;

      if ((i + j) % 2 == 0) { // Even stripes
        if (j < width-1)
          j++;
        else
          i+= 2;
        if (i > 0)
          i--;
      }
      else { // Odd stripes
        if (i < height-1)
          i++;
        else
          j+= 2;
        if (j > 0)
          j--;
      }
    }

    return zigZag;
  }

}
