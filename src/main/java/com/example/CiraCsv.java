package com.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.example.CiraRec.ItemType;
import com.example.CiraRec.ProcMethod;

import au.com.bytecode.opencsv.CSVReader;

public class CiraCsv {
   private static final TimeZone TZ = TimeZone.getTimeZone("UTC");

   private List<CiraRec> items;
   private BigDecimal totalAmount;

   // The following are only used during instance construction,
   // but are kept as member variables for ease of access:
   private int lineNum;
   private SimpleDateFormat sdf1;
     
   public CiraCsv(String filename) throws IOException, ParseException {
      init(new File(filename));
   }

   public CiraCsv(File file) throws IOException, ParseException {
      init(file);
   }

   private SimpleDateFormat prepSdf(String format) {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      sdf.setTimeZone(TZ);
      sdf.setLenient(false);
      return sdf;
   }

   private void assertFieldCount(String[] fields, int count) 
      throws ParseException {
      if (fields.length != count) {
         throw new ParseException(String.format(
            "Expected %d fields on line %d. Found %d",
            count, lineNum, fields.length), lineNum);
      }
   }

   private BigDecimal parseExpectedTotal(String[] fields)
      throws ParseException {
      assertFieldCount(fields, 2);
      BigDecimal bd = null;
      try {
         bd = new BigDecimal(fields[1]);
      } catch (NumberFormatException e) {
         throw new ParseException("Error parsing expected total", lineNum);
      }
      return bd;
   }

   private int parseExpectedItemCount(String[] fields)
      throws ParseException {
      assertFieldCount(fields, 2);
      int count = -1;
      try {
         count = Integer.parseInt(fields[1]);
      } catch (NumberFormatException e) {
         throw new ParseException(
            "Error parsing expected item count", lineNum);
      }
      return count;
   }

   private void init(File file) throws IOException, ParseException {
      CSVReader reader = null;
      int lineNum = 1;
      try {
         sdf1 = prepSdf("yyyy-MM-dd HH:mm:ss");
         reader = new CSVReader(new FileReader(file));
         items = new ArrayList<CiraRec>();
         totalAmount = new BigDecimal(0);
         int expectedItemCount = -1;
         BigDecimal expectedTotalAmount = null;
         String[] fields;
         while ((fields = reader.readNext()) != null) {
            switch (lineNum) {
               case 1:
               case 2:
                  //currently ignored
                  break;
               case 3:
                  expectedTotalAmount = parseExpectedTotal(fields);
                  break;
               case 4:
                  expectedItemCount = parseExpectedItemCount(fields);
                  break;
               case 5:
                  //TODO handle 65K error msg
                  break;
               default:
                  CiraRec rec = parseRec(fields);
                  items.add(rec);
                  totalAmount.add(rec.getAmount());
                  break;
            }
            lineNum++;
         }
         if (!totalAmount.equals(expectedTotalAmount)) {
            throw new IllegalArgumentException("Stated total does not " +
                      "match the actual total of the line items.");
         }
         if (items.size() != expectedItemCount) {
            throw new IllegalArgumentException("Stated total item count " +
                      "does not match the actual count of line items.");
         }
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
   }

   private CiraRec parseRec(String[] fields) throws ParseException {
      assertFieldCount(fields, 44);
      CiraRec r = new CiraRec();
      int fieldIndex = 0;
      r.setIrn(fields[fieldIndex++]);
      r.setOtcEndpoint(fields[fieldIndex++]);
      r.setAlc2(fields[fieldIndex++]);
      r.setCaptureDate(sdf1.parse(fields[fieldIndex++]));
      r.setReceiveDate(sdf1.parse(fields[fieldIndex++]));
      try {
         r.setTransitNumber(Long.parseLong(fields[fieldIndex++]));
      } catch (NumberFormatException e) {
         throw new ParseException("Error parsing transit number", lineNum);
      }
      try {
         r.setCheckNumber(Integer.parseInt(fields[fieldIndex++]));
      } catch (NumberFormatException e) {
         throw new ParseException("Error parsing check number", lineNum);
      }
      try {
         r.setAccount(Long.parseLong(fields[fieldIndex++]));
      } catch (NumberFormatException e) {
         throw new ParseException("Error parsing account", lineNum);
      }
      try {
         String amt = fields[fieldIndex++];
         if (amt.startsWith("$")) {
            amt = amt.substring(1);
         }
         r.setAmount(new BigDecimal(amt));
      } catch (NumberFormatException e) {
         throw new ParseException("Error parsing amount", lineNum);
      }
      r.setCashierId(fields[fieldIndex++]);
      try {
         r.setItemType(ItemType.valueOf(
            fields[fieldIndex++].toUpperCase().replaceAll("[ -]", "_")));
      } catch (IllegalArgumentException e) {
         throw new ParseException("Error parsing item type", lineNum);
      }
      try {
         r.setProcMethod(ProcMethod.valueOf(
            fields[fieldIndex++].toUpperCase().replaceAll("[ -]", "_")));
      } catch (IllegalArgumentException e) {
         throw new ParseException("Error parsing process method", lineNum);
      }
      r.setBatchId(fields[fieldIndex++]);
      r.setSettlementDate(sdf1.parse(fields[fieldIndex++]));
      r.setReturnSettlementDate(sdf1.parse(fields[fieldIndex++]));
      r.setVoucherNumber(fields[fieldIndex++]);
      try {
         r.setTicketNumber(Long.parseLong(fields[fieldIndex++]));
      } catch (NumberFormatException e) {
         throw new ParseException("Error parsing ticket number", lineNum);
      }
      for (int i = 0; i < CiraRec.NUM_USER_FIELDS; i++) {
         r.setUserField(i, fields[fieldIndex++]);
      }
      r.setAgencyAcctCode(fields[fieldIndex++]);
      r.setDescription(fields[fieldIndex++]);
      return r;
   }

   public static void main(String[] args) throws Exception {
      CiraCsv cc = new CiraCsv(args[0]);
      System.out.println("ttl  amount: " + cc.getTotalAmount());
      System.out.println("ttl   count: " + cc.getItemCount());
      System.out.println("1st chk num: " + cc.getItem(1).getCheckNumber());
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
