package com.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CiraCsv {

   private List<CiraRec> items;
   private BigDecimal totalAmount;

   public CiraCsv(String filename) throws IOException, ParseException {
      init(new File(filename));
   }

   public CiraCsv(File file) throws IOException, ParseException {
      init(file);
   }

   private void assertFieldCount(String[] fields, int count, int lineNum) 
      throws ParseException {
      if (fields.length != count) {
         throw new ParseException(String.format(
            "Expected %d fields on line %d. Found %d",
            fields.length, lineNum, count), lineNum);
      }
   }

   private void init(File file) throws IOException, ParseException {
      CSVReader reader = null;
      int lineNum  = 1; //used for error reporting
      int fieldNum = 1; //used for error reporting
      try {
         reader = new CSVReader(new FileReader(file));
         items = new ArrayList<CiraRec>();
         totalAmount = new BigDecimal(0);
         int expectedItemCount = -1;
         BigDecimal expectedTotalAmount = null;
         String[] fields;
         while ((fields = reader.readNext()) != null) {
            if (lineNum == 3) {
               assertFieldCount(fields, 2, lineNum);
               fieldNum = 2;
               expectedTotalAmount = new BigDecimal(fields[1]);
            }
            if (lineNum == 4) {
               assertFieldCount(fields, 2, lineNum);
               fieldNum = 2;
               expectedItemCount = Integer.parseInt(fields[1]);
            }
            lineNum++;
         }
      } catch (NumberFormatException e) {
         throw new ParseException(String.format(
            "NumberFormatException while parsing field %d on line %d",
            fieldNum, lineNum), lineNum);
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
   }

   public static void main(String[] args) throws Exception {
      CiraCsv cc = new CiraCsv(args[0]);
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
