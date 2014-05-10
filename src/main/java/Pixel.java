import compressor.image.Channel;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * License []
 * Year: 2014
 */
public class Pixel {
  private int mAlpha;
  private int mRed;
  private int mGreen;
  private int mBlue;

  public Pixel(int r, int g, int b) {
    mAlpha = 255;
    mRed = r;
    mGreen = g;
    mBlue = b;
  }

  public Pixel(int a, int r, int g, int b) {
    mAlpha = a;
    mRed = r;
    mGreen = g;
    mBlue = b;
  }

  public Pixel(int rgb) {
    mAlpha = (rgb >> 24) & 0x000000FF;
    mRed = (rgb >> 16) & 0x000000FF;
    mGreen = (rgb >> 8) & 0x000000FF;
    mBlue = (rgb) & 0x000000FF;
  }

  public int getAlpha() {
    return mAlpha;
  }

  public void setAlpha(int alpha) {
    this.mAlpha = alpha;
  }

  public int getGreen() {
    return mGreen;
  }

  public void setGreen(int green) {
    this.mGreen = green;
  }

  public int getBlue() {
    return mBlue;
  }

  public void setBlue(int blue) {
    this.mBlue = blue;
  }

  public int getRed() {
    return mRed;
  }

  public void setRed(int red) {
    this.mRed = red;
  }

  public int toRGB() {
    int rgb = ((mRed & 0xff) << 16); // red
    rgb += ((mGreen & 0xff) << 8); // green
    rgb += (mBlue & 0xff); // blue

    return rgb;
  }

  public int toARGB() {
    int argb = ((mAlpha & 0xff) << 24); // alpha
    argb += ((mRed & 0xff) << 16); // red
    argb += ((mGreen & 0xff) << 8); // green
    argb += (mBlue & 0xff); // blue

    return argb;
  }

  public int getChannel(Channel channel) {
    switch (channel) {
      case Red:
        return getRed();
      case Green:
        return getGreen();
      case Blue:
        return getBlue();
    }
    return 255;
  }
}
