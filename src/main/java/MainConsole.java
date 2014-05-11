import compressor.Compressor;
import compressor.image.CJPEG;
import compressor.image.JPEG;

import java.io.IOException;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class MainConsole {

  public static void main(String[] args) {
    String inputFile = MainConsole.class.getResource("/sample1.jpg").getPath();

    try {
      JPEG jpeg = JPEG.fromFile(inputFile);

      Compressor compressor = new Compressor(5);
      CJPEG cjpeg = compressor.compressImage(jpeg);

      cjpeg.toFile("compressed.cjpg");

      cjpeg = CJPEG.fromFile("compressed.cjpg");
      jpeg = compressor.decompressImage(cjpeg);
      jpeg.toFile("restored.jpg");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
