package com.example;

public class CiraException extends Exception {
   private static final long serialVersionUID = 1L;

   public CiraException(String message) {
      super(message);
   }

   public CiraException(String message, Throwable cause) {
      super(message, cause);
   }

   public CiraException(String message, int lineNum) {
      super(String.format("Line %d: %s", lineNum, message));
   }

   public CiraException(String message, int lineNum, Throwable cause) {
      super(String.format("Line %d: %s", lineNum, message), cause);
   }
}
