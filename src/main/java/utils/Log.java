package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Author: Victor Kifer (droiddevua[at]gmail[dot]com)
 * Year: 2014
 */
public class Log {
  private static final boolean DEBUG = true;

  public static void e(String message) {
    if(DEBUG)
      print("E", message);
  }

  public static void e(int message) {
    if(DEBUG)
      print("E", message+"");
  }

  public static void i(String message) {
    if(DEBUG)
      print("I", message);
  }

  public static void i(int message) {
    if(DEBUG)
      print("I", message+"");
  }

  private static void print(String tag, String message) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    System.out.println(tag + " - " + sdf.format(cal.getTime()) + " - " + message);
  }
}
