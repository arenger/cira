package com.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CiraCsv {

   private List<CiraRec> items;
   private BigDecimal totalAmount;

   public CiraCsv(String filename) throws IOException {
      init(new File(filename));
   }

   public CiraCsv(File file) throws IOException {
      init(file);
   }

   private void init(File file) throws IOException {
      CSVReader reader = null;
      try {
         reader = new CSVReader(new FileReader(file));
         String[] nextLine;
         while ((nextLine = reader.readNext()) != null) {
            System.out.println(nextLine[0] + nextLine[1] + "etc...");
         }
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
   }

   public CiraRec getItem(int index) {
      return items.get(index);
   }

   public BigDecimal getTotalAmount() {
      return totalAmount;
   }

   public int getItemCount() {
      return items.size();
   }
}
