package compressor.internal;

public class Config {
  private static final int BLOCK_SIZE = 8;

  public static final byte DEFAULT_QUALITY = 10;

  public static int getBlockSize() {
    return BLOCK_SIZE;
  }
}
