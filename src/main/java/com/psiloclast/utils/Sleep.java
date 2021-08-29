package com.psiloclast.utils;

public class Sleep {
  public static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
