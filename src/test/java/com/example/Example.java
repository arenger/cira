package com.example;

public class Example {
   public static void main(String[] args) throws Exception {
      CiraCsv cc = new CiraCsv(args[0]);
      System.out.println("Total Amount: " + cc.getTotalAmount());
      System.out.println(" Total Count: " + cc.getItemCount());
      System.out.println(" 1st Check #: " + cc.getItem(0).getCheckNumber());
   }
}
