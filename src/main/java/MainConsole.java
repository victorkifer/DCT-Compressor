import compressor.Compressor;
import compressor.image.CImage;
import compressor.image.JPEG;
import compressor.internal.Config;

import java.io.IOException;

public class MainConsole {

  public static void main(String[] args) {
    String inputFile = MainConsole.class.getResource("/sample1.jpg").getPath();

    try {
      JPEG jpeg = JPEG.fromFile(inputFile);

      Compressor compressor = new Compressor(Config.DEFAULT_QUALITY);

      CImage cImage = compressor.compressImage(jpeg);

      cImage.toFile("compressed.cimg");

      cImage = CImage.fromFile("compressed.cimg");
      jpeg = compressor.decompressImage(cImage);

      jpeg = compressor.test(jpeg);
      jpeg.toFile("restored.jpg");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
