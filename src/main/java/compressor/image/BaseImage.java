package compressor.image;

import compressor.internal.Config;

import java.io.IOException;

public abstract class BaseImage {
  protected int width;
  protected int height;

  protected int normalizedWidth;
  protected int normalizedHeight;

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  protected void normalize() {
    final int N = Config.getBlockSize();
    int a = 0,
        b = 0;

    if(width%N != 0)
      a = N - width%N;
    if(height%N != 0)
      b = N - height%N;

    normalizedWidth = width + a;
    normalizedHeight = height + b;
  }

  public int getNormalizedWidth() {
    return normalizedWidth;
  }

  public int getNormalizedHeight() {
    return normalizedHeight;
  }

  public abstract void toFile(String filename) throws IOException;
}
