package com.psiloclast.utils;

public class Sleep {
  public static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      System.out.println(e);
    }
  }
}
