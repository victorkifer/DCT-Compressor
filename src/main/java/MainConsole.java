import compressor.Compressor;
import compressor.DCT;
import compressor.image.CJPEG;
import compressor.image.Channel;
import compressor.image.JPEG;
import utils.MatrixUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class MainConsole {

  static ProcessingImage image;
  static DCT dct;

  public static void main(String[] args) {
    String inputFile = MainConsole.class.getResource("/sample1.jpg").getPath();

    try {
      JPEG jpeg = JPEG.fromFile(inputFile);

      Compressor compressor = new Compressor(10);
      CJPEG cjpeg = compressor.compressImage(jpeg);

      cjpeg.toFile("compressed.jpg");
    } catch (IOException e) {
      e.printStackTrace();
    }

    /*
    String compressedFile = "compressed.jpg";
    String restoredFile = "restored.jpg";

    try {
      image = ProcessingImage.fromFile(inputFile);
      dct = new DCT(10);

      compressChannel(Channel.Red);
      compressChannel(Channel.Green);
      compressChannel(Channel.Blue);

      MatrixUtils.displayMatrix(image.getChannel(Channel.Red));

      if(5>0)
        return;

      writeImage(new File(compressedFile));

      decompressChannel(Channel.Red);
      decompressChannel(Channel.Green);
      decompressChannel(Channel.Blue);

      writeImage(new File(restoredFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
    */
  }

  public static void compressImage() {
    byte[] redBytes = compressChannel2(Channel.Red);
    byte[] greenBytes = compressChannel2(Channel.Green);
    byte[] blueBytes = compressChannel2(Channel.Blue);
  }

  public static byte[] compressChannel2(Channel channel) {
    int w = image.getWidth() / DCT.getBlockSize();
    int h = image.getHeight() / DCT.getBlockSize();

    int[][] channelValue = image.getChannel(Channel.Red);

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(channelValue, DCT.getBlockSize(), j, i);
        chunk = dct.forwardDCT(chunk);
        chunk = dct.quantizeDCT(chunk, true);
        MatrixUtils.setChunk(channelValue, chunk, j, i);
      }
    }

    return dct.compressImage(MatrixUtils.toVector(channelValue), true);
  }

  public static void writeImage(File file) throws IOException {
    // ImageIO.write(image.toBufferedImage(), "jpg", file);
    // or
    ImageOutputStream ios =  ImageIO.createImageOutputStream(file);
    ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
    ImageWriteParam param = writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // Needed see javadoc
    param.setCompressionQuality(1.0F); // Highest quality
    writer.setOutput(ios);
    writer.write(null, new IIOImage(image.toBufferedImage(), null, null), param); // image.toBufferedImage());
    writer.dispose();
  }

  public static void compressChannel(Channel channel) {
    int w = image.getWidth() / DCT.getBlockSize();
    int h = image.getHeight() / DCT.getBlockSize();

    int[][] channelValue = image.getChannel(channel);

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(channelValue, DCT.getBlockSize(), j, i);
        chunk = dct.forwardDCT(chunk);
        chunk = dct.quantizeDCT(chunk, true);
        MatrixUtils.setChunk(channelValue, chunk, j, i);
      }
    }

    image.setChannel(channel, channelValue);
  }

  public static void decompressChannel(Channel channel) {
    int w = image.getWidth() / DCT.getBlockSize();
    int h = image.getHeight() / DCT.getBlockSize();

    int[][] channelValue = image.getChannel(channel);

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(channelValue, DCT.getBlockSize(), j, i);
        chunk = dct.dequantizeDCT(chunk, true);
        chunk = dct.inverseDCT(chunk);
        MatrixUtils.setChunk(channelValue, chunk, j, i);
      }
    }

    image.setChannel(channel, channelValue);
  }

}
