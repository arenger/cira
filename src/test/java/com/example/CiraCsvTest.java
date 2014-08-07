package com.example;

import java.io.File;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

public class CiraCsvTest {

   private static final String EV = "CIRA_HOME";
   private String home;

   @BeforeClass
   public void environment() throws Exception {
      home = System.getenv(EV);
      if (home == null) {
         throw new Exception(EV + " not yet");
      }
      if (!(new File(home).exists())) {
         throw new Exception(String.format(
            "Does not exist: %s.  Check the %s environmnet variable",
            home, EV));
      }
   }

   @Test
   public void happyPath() {
      CiraCsv cc = new CiraCsv(home + "/sample.csv");
      Assert.assertTrue("Should be 2 records", cc.getItemCount() == 2);
      Assert.assertTrue("Total should be $50", cc.getTotalAmount() == 50);
   }
}
