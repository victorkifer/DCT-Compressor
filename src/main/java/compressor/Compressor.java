package compressor;

import compressor.image.CImage;
import compressor.image.Channel;
import compressor.image.JPEG;
import compressor.internal.*;
import utils.Log;
import utils.MatrixUtils;

public class Compressor {

  DCT dctPerformer;
  Quantizer quantizer;
  RunLengthCompressor compressor;

  public Compressor() {
    this(Config.DEFAULT_QUALITY);
  }

  public Compressor(byte quality) {
    dctPerformer = new DCT();
    quantizer = new Quantizer(quality);
    compressor = new RunLengthCompressor();
  }

  public void setQuality(byte quality) {
    quantizer.reset(quality);
  }

  public JPEG test(JPEG image) {
    int[][] red = image.getChannel(Channel.Red);
    int[][] green = image.getChannel(Channel.Green);
    int[][] blue = image.getChannel(Channel.Blue);

    Log.i("Performing ForwardDCT");
    performForwardDCT(red);
    performForwardDCT(green);
    performForwardDCT(blue);

    Log.i("Performing quantization. Quality equals to " + quantizer.getQuality());
    performQuantization(red);
    performQuantization(green);
    performQuantization(blue);

    Log.i("Performing matrix to vector conversion");
    int[] redArr = MatrixUtils.toVector(red);
    int[] greenArr = MatrixUtils.toVector(green);
    int[] blueArr = MatrixUtils.toVector(blue);

    byte[] redBytes = compressor.compressImage(redArr);
    byte[] greenBytes = compressor.compressImage(greenArr);
    byte[] blueBytes = compressor.compressImage(blueArr);

    redArr = compressor.decompressImage(redBytes, redArr.length);
    greenArr = compressor.decompressImage(greenBytes, greenArr.length);
    blueArr = compressor.decompressImage(blueBytes, blueArr.length);

    Log.i("Performing vector to matrix conversion");
    red = MatrixUtils.fromVector(redArr, image.getNormalizedWidth(), image.getNormalizedHeight());
    green = MatrixUtils.fromVector(greenArr, image.getNormalizedWidth(), image.getNormalizedHeight());
    blue = MatrixUtils.fromVector(blueArr, image.getNormalizedWidth(), image.getNormalizedHeight());

    Log.i("Performing dequantization");
    performDequantization(red);
    performDequantization(green);
    performDequantization(blue);

    Log.i("Performing InverseDCT");
    performInverseDCT(red);
    performInverseDCT(green);
    performInverseDCT(blue);

    JPEG.Builder builder = new JPEG.Builder();
    builder.originalHeight(image.getHeight());
    builder.originalWidth(image.getWidth());
    builder.redMask(red);
    builder.greenMask(green);
    builder.blueMask(blue);

    return builder.build();
  }

  public CImage compressImage(JPEG image) {
    int[][] red = image.getChannel(Channel.Red);
    int[][] green = image.getChannel(Channel.Green);
    int[][] blue = image.getChannel(Channel.Blue);

    Log.i("Performing ForwardDCT");
    performForwardDCT(red);
    performForwardDCT(green);
    performForwardDCT(blue);

    Log.i("Performing quantization. Quality equals to " + quantizer.getQuality());
    performQuantization(red);
    performQuantization(green);
    performQuantization(blue);

    Log.i("Performing matrix to vector conversion");
    int[] redArr = MatrixUtils.toVector(red);
    int[] greenArr = MatrixUtils.toVector(green);
    int[] blueArr = MatrixUtils.toVector(blue);

    Log.i("Performing RLE compression");
    byte[] redArr2 = compressor.compressImage(redArr);
    byte[] greenArr2 = compressor.compressImage(greenArr);
    byte[] blueArr2 = compressor.compressImage(blueArr);

    CImage.Builder builder = new CImage.Builder();
    builder.height(image.getHeight());
    builder.width(image.getWidth());
    builder.redMask(redArr2);
    builder.greenMask(greenArr2);
    builder.blueMask(blueArr2);
    builder.quality(quantizer.getQuality());

    return builder.build();
  }

  private void performForwardDCT(int[][] channelValue) {
    int h = channelValue.length / Config.getBlockSize();
    int w = channelValue[0].length / Config.getBlockSize();

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(channelValue, Config.getBlockSize(), j, i);
        chunk = dctPerformer.forwardDCT(chunk);
        MatrixUtils.setChunk(channelValue, chunk, j, i);
      }
    }
  }

  private void performQuantization(int[][] dctChannel) {
    int h = dctChannel.length / Config.getBlockSize();
    int w = dctChannel[0].length / Config.getBlockSize();

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(dctChannel, Config.getBlockSize(), j, i);
        chunk = quantizer.quantizeDCT(chunk, true);
        MatrixUtils.setChunk(dctChannel, chunk, j, i);
      }
    }
  }

  public JPEG decompressImage(CImage image) {
    byte[] redArr2 = image.getCompressedChannel(Channel.Red);
    byte[] greenArr2 = image.getCompressedChannel(Channel.Green);
    byte[] blueArr2 = image.getCompressedChannel(Channel.Blue);

    Log.i("Performing RLE decompression");
    int imageLength = image.getNormalizedHeight()*image.getNormalizedWidth();
    int[] redArr = compressor.decompressImage(redArr2, imageLength);
    int[] greenArr = compressor.decompressImage(greenArr2, imageLength);
    int[] blueArr = compressor.decompressImage(blueArr2, imageLength);

    Log.i("Performing vector to matrix conversion");
    int[][] red = MatrixUtils.fromVector(redArr, image.getNormalizedWidth(), image.getNormalizedHeight());
    int[][] green = MatrixUtils.fromVector(greenArr, image.getNormalizedWidth(), image.getNormalizedHeight());
    int[][] blue = MatrixUtils.fromVector(blueArr, image.getNormalizedWidth(), image.getNormalizedHeight());

    Log.i("Performing dequantization");
    byte originQuality = quantizer.getQuality();
    quantizer.reset(image.getQuality());
    performDequantization(red);
    performDequantization(green);
    performDequantization(blue);
    quantizer.reset(originQuality);

    Log.i("Performing InverseDCT");
    performInverseDCT(red);
    performInverseDCT(green);
    performInverseDCT(blue);

    JPEG.Builder builder = new JPEG.Builder();
    builder.originalHeight(image.getHeight());
    builder.originalWidth(image.getWidth());
    builder.redMask(red);
    builder.greenMask(green);
    builder.blueMask(blue);

    return builder.build();
  }

  private void performInverseDCT(int[][] channelValue) {
    int h = channelValue.length / Config.getBlockSize();
    int w = channelValue[0].length / Config.getBlockSize();

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(channelValue, Config.getBlockSize(), j, i);
        chunk = dctPerformer.inverseDCT(chunk);
        MatrixUtils.setChunk(channelValue, chunk, j, i);
      }
    }
  }

  private void performDequantization(int[][] dctChannel) {
    int h = dctChannel.length / Config.getBlockSize();
    int w = dctChannel[0].length / Config.getBlockSize();

    for(int i = 0; i < h; i++) {
      for(int j = 0; j < w; j++) {
        int [][]chunk = MatrixUtils.getChunk(dctChannel, Config.getBlockSize(), j, i);
        chunk = quantizer.dequantizeDCT(chunk, true);
        MatrixUtils.setChunk(dctChannel, chunk, j, i);
      }
    }
  }
}
