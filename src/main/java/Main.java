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


    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
