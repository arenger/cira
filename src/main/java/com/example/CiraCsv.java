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
     
   public CiraCsv(String filename) throws IOException, CiraException {
      init(new File(filename));
   }

   public CiraCsv(File file) throws IOException, CiraException {
      init(file);
   }

   private void init(File file) throws IOException, CiraException {
      CSVReader reader = null;
      lineNum = 1;
      sdf1 = prepSdf("yyyy-MM-dd HH:mm:ss");
      try {
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
               case 5:
                  //currently ignored
                  break;
               case 3:
                  expectedTotalAmount = parseExpectedTotal(fields);
                  break;
               case 4:
                  //TODO handle possible 65K error msg
                  expectedItemCount = parseExpectedItemCount(fields);
                  break;
               default:
                  CiraRec rec = parseRec(fields);
                  items.add(rec);
                  totalAmount = totalAmount.add(rec.getAmount());
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

   private SimpleDateFormat prepSdf(String format) {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      sdf.setTimeZone(TZ);
      sdf.setLenient(false);
      return sdf;
   }

   private void assertFieldCount(String[] fields, int count) 
      throws CiraException {
      if (fields.length != count) {
         throw new CiraException(String.format(
            "Expected %d fields on line %d. Found %d",
            count, lineNum, fields.length), lineNum);
      }
   }

   private BigDecimal parseExpectedTotal(String[] fields)
      throws CiraException {
      assertFieldCount(fields, 2);
      BigDecimal bd = null;
      try {
         bd = new BigDecimal(fields[1]);
      } catch (NumberFormatException e) {
         throw new CiraException("Error parsing expected total", lineNum);
      }
      return bd;
   }

   private int parseExpectedItemCount(String[] fields)
      throws CiraException {
      assertFieldCount(fields, 2);
      int count = -1;
      try {
         count = Integer.parseInt(fields[1]);
      } catch (NumberFormatException e) {
         throw new CiraException(
            "Error parsing expected item count", lineNum);
      }
      return count;
   }

   private boolean isSet(String str) {
      return ((str != null) && (str.length() != 0) &&
              !str.equalsIgnoreCase("null"));
   }

   private CiraRec parseRec(String[] fields) throws CiraException {
      assertFieldCount(fields, 44);
      CiraRec r = new CiraRec();
      int fieldIndex = 0;

      String field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setIrn(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setOtcEndpoint(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setAlc2(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setCaptureDate(sdf1.parse(field));
         } catch (ParseException e) {
            throw new CiraException("Capture Date", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setReceiveDate(sdf1.parse(field));
         } catch (ParseException e) {
            throw new CiraException("Receive Date", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setTransitNumber(Long.parseLong(field));
         } catch (NumberFormatException e) {
            throw new CiraException("Transit Number", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setCheckNumber(Integer.parseInt(field));
         } catch (NumberFormatException e) {
            throw new CiraException("Check Number", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setAccount(Long.parseLong(field));
         } catch (NumberFormatException e) {
            throw new CiraException("Account", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            if (field.startsWith("$")) {
               field = field.substring(1);
            }
            r.setAmount(new BigDecimal(field));
         } catch (NumberFormatException e) {
            throw new CiraException("Amount", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setCashierId(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setItemType(ItemType.valueOf(
               field.toUpperCase().replaceAll("[ -]", "_")));
         } catch (IllegalArgumentException e) {
            throw new CiraException("Item Type", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setProcMethod(ProcMethod.valueOf(
               field.toUpperCase().replaceAll("[ -]", "_")));
         } catch (IllegalArgumentException e) {
            throw new CiraException("Process Method", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setBatchId(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setSettlementDate(sdf1.parse(field));
         } catch (ParseException e) {
            throw new CiraException("Process Method", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setReturnSettlementDate(sdf1.parse(field));
         } catch (ParseException e) {
            throw new CiraException("Settlement Date", lineNum, e);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setVoucherNumber(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         try {
            r.setTicketNumber(Long.parseLong(field));
         } catch (NumberFormatException e) {
            throw new CiraException("Ticket Number", lineNum, e);
         }
      }

      for (int i = 0; i < CiraRec.NUM_USER_FIELDS; i++) {
         field = fields[fieldIndex++];
         if (isSet(field)) { 
            r.setUserField(i, field);
         }
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setAgencyAcctCode(field);
      }

      field = fields[fieldIndex++];
      if (isSet(field)) { 
         r.setDescription(field);
      }
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
