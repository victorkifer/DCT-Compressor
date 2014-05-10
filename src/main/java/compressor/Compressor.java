package compressor;

import compressor.image.CJPEG;
import compressor.image.Channel;
import compressor.image.JPEG;
import compressor.internal.*;
import utils.MatrixUtils;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class Compressor {

  DCTPerformer dctizer;
  QuantizationPerformer quantizer;
  RunCounterCompressionPerformer compressor;

  public Compressor() {
    this(10);
  }

  public Compressor(int quality) {
    dctizer = new DCTPerformer();
    quantizer = new QuantizationPerformer(quality);
    compressor = new RunCounterCompressionPerformer();
  }

  public void setQuality(int quality) {
    quantizer.reset(quality);
  }

  public CJPEG compressImage(JPEG image) {
    int[][] red = image.getChannel(Channel.Red);
    int[][] green = image.getChannel(Channel.Green);
    int[][] blue = image.getChannel(Channel.Blue);

    performForwardDCT(red);
    performForwardDCT(green);
    performForwardDCT(blue);

    performQuantization(red);
    performQuantization(green);
    performQuantization(blue);

    int[] redArr = MatrixUtils.toVector(red);
    int[] greenArr = MatrixUtils.toVector(green);
    int[] blueArr = MatrixUtils.toVector(blue);

    redArr = compressor.compressImage(redArr);
    greenArr = compressor.compressImage(greenArr);
    blueArr = compressor.compressImage(blueArr);

    CJPEG.Builder builder = new CJPEG.Builder();
    builder.height(image.getHeight());
    builder.width(image.getWidth());
    builder.redMask(redArr);
    builder.greenMask(greenArr);
    builder.blueMask(blueArr);

    return builder.build();
  }

  private void performForwardDCT(int[][] channelValue) {
    int h = channelValue.length / DCT.getBlockSize();
    int w = channelValue[0].length / DCT.getBlockSize();

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(channelValue, DCT.getBlockSize(), j, i);
        chunk = dctizer.forwardDCT(chunk);
        MatrixUtils.setChunk(channelValue, chunk, j, i);
      }
    }
  }

  private void performQuantization(int[][] dctChannel) {
    int h = dctChannel.length / DCT.getBlockSize();
    int w = dctChannel[0].length / DCT.getBlockSize();

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(dctChannel, DCT.getBlockSize(), j, i);
        chunk = quantizer.quantizeDCT(chunk, true);
        MatrixUtils.setChunk(dctChannel, chunk, j, i);
      }
    }
  }

  public JPEG decompressImage(CJPEG image) {
    int[] redArr = image.getCompressedChannel(Channel.Red);
    int[] greenArr = image.getCompressedChannel(Channel.Green);
    int[] blueArr = image.getCompressedChannel(Channel.Blue);

    int imageLength = image.getHeight()*image.getWidth();
    redArr = compressor.decompressImage(redArr, imageLength);
    greenArr = compressor.decompressImage(greenArr, imageLength);
    blueArr = compressor.decompressImage(blueArr, imageLength);

    int[][] red = MatrixUtils.fromVector(redArr, image.getWidth(), image.getHeight());
    int[][] green = MatrixUtils.fromVector(greenArr, image.getWidth(), image.getHeight());
    int[][] blue = MatrixUtils.fromVector(blueArr, image.getWidth(), image.getHeight());

    red = quantizer.dequantizeDCT(red, true);
    green = quantizer.dequantizeDCT(green, true);
    blue = quantizer.dequantizeDCT(blue, true);

    red = dctizer.inverseDCT(red);
    green = dctizer.inverseDCT(green);
    blue = dctizer.inverseDCT(blue);

    JPEG.Builder builder = new JPEG.Builder();
    builder.originalHeight(image.getHeight());
    builder.originalWidth(image.getWidth());
    builder.redMask(red);
    builder.greenMask(green);
    builder.blueMask(blue);

    return builder.build();
  }
}
