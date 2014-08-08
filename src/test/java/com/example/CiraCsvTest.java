package com.example;

import java.io.File;
import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import com.example.CiraRec.ItemType;
import com.example.CiraRec.ProcMethod;

public class CiraCsvTest {

   private static final String EV = "CIRA_HOME";

   private String  home;
   private CiraCsv sample;

   public CiraCsvTest() throws Exception {
      home = System.getenv(EV);
      if (home == null) {
         throw new Exception(EV + " not yet");
      }
      if (!(new File(home).exists())) {
         throw new Exception(String.format(
            "Does not exist: %s.  Check the %s environment variable",
            home, EV));
      }
      sample = new CiraCsv(home + "/src/main/etc/sample.csv");
   }

   @Test
   public void checkSampleTotal() throws Exception {
      Assert.assertEquals(sample.getTotalAmount(), new BigDecimal("40.00"));
   }

   @Test
   public void checkSampleCount() throws Exception {
      Assert.assertEquals(sample.getItemCount(), 2);
   }

   @Test
   public void checkSampleIRN() throws Exception {
      Assert.assertEquals(sample.getItem(0).getIrn(), "13358778940026025587");
   }

   @Test
   public void checkSampleOtcEndpoint() throws Exception {
      Assert.assertEquals(sample.getItem(0).getOtcEndpoint(), "DFAS");
   }

   @Test
   public void checkSampleAlc() throws Exception {
      Assert.assertEquals(sample.getItem(0).getAlc2(), "619201");
   }

   @Test
   public void checkSampleCaptureDate() throws Exception {
      Assert.assertEquals(sample.getItem(0).getCaptureDate().getTime(),
            1335863494000L); //2012-05-01 09:11:34
   }

   @Test
   public void checkSampleReceiveDate() throws Exception {
      Assert.assertEquals(sample.getItem(1).getReceiveDate().getTime(),
            1264032000000L); //2010-01-21 00:00:00
   }

   @Test
   public void checkSampleTransitNumber() throws Exception {
      Assert.assertEquals(sample.getItem(0).getTransitNumber(), 31100209);
   }

   @Test
   public void checkSampleCheckNumber() throws Exception {
      Assert.assertEquals(sample.getItem(0).getCheckNumber(), 132284);
   }

   @Test
   public void checkSampleAccount() throws Exception {
      Assert.assertEquals(sample.getItem(0).getAccount(), 12345678);
   }

   @Test
   public void checkSampleAmount() throws Exception {
      Assert.assertEquals(sample.getItem(0).getAmount(),
         new BigDecimal("25.00"));
   }

   @Test
   public void checkSampleCashierId() throws Exception {
      Assert.assertEquals(sample.getItem(0).getCashierId(), "qcco0003");
   }

   @Test
   public void checkSampleItemType() throws Exception {
      Assert.assertEquals(sample.getItem(0).getItemType(), ItemType.PERSONAL);
   }

   @Test
   public void checkSampleProcMethod() throws Exception {
      Assert.assertEquals(sample.getItem(0).getProcMethod(),
            ProcMethod.CUSTOMER_NOT_PRESENT);
   }

   @Test
   public void checkSampleBatchId() throws Exception {
      Assert.assertEquals(sample.getItem(0).getBatchId(),
         "C3DCD818-AD0E-41AC-B272-8D3756BADCD7");
   }

   @Test
   public void checkSampleSettleDate() throws Exception {
      Assert.assertEquals(sample.getItem(0).getSettlementDate().getTime(),
            1336521600000L); //2012-05-09 00:00:00
   }

   @Test
   public void checkSampleRetSettleDate() throws Exception {
      Assert.assertEquals(sample.getItem(0).getReturnSettlementDate()
            .getTime(), 1336521603000L); //2012-05-09 00:00:03
   }

   @Test
   public void checkSampleVoucherNumber() throws Exception {
      Assert.assertEquals(sample.getItem(0).getVoucherNumber(), "4");
   }

   @Test
   public void checkSampleTicketNumber() throws Exception {
      Assert.assertEquals(sample.getItem(0).getTicketNumber(), 903);
   }

   @Test
   public void checkSampleUserFields() throws Exception {
      Assert.assertEquals(sample.getItem(1).getUserField(0), "01/17/2010");
      Assert.assertEquals(sample.getItem(1).getUserField(1), "STR");
   }

   @Test
   public void checkSampleAgencyAcctCode() throws Exception {
      Assert.assertEquals(sample.getItem(0).getAgencyAcctCode(), "97X8358.1");
   }

   @Test
   public void checkSampleDescription() throws Exception {
      Assert.assertEquals(sample.getItem(0).getDescription(), "desc");
   }
}
