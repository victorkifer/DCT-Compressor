import compressor.DCT;
import utils.MatrixUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class Main {

  public static void main(String[] args) {
    String inputFile = Main.class.getResource("/input.jpg").getPath();
    String compressedFile = "compressed.jpg";
    String restoredFile = "respored.jpg";

    try {
      URL url = new File(inputFile).toURI().toURL();
      BufferedImage image = ImageIO.read(url);

      int[][] arr = new int[image.getHeight()][image.getWidth()];

      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          arr[i][j] = image.getRGB(i, j);
        }
      }
      MatrixUtils.displayMatrix(arr);

      DCT dct = new DCT(50);

      int[][] dctArr = dct.forwardDCT(arr);
      dctArr = dct.quantizeDCT(dctArr, false);
      MatrixUtils.displayMatrix(dctArr);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
