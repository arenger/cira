package com.example;

import java.math.BigDecimal;
import java.util.Date;

public class CiraRec {
   public static final int NUM_USER_FIELDS = 24;

   private String     irn;
   private String     otcEndpoint;
   private String     alc2;
   private Date       captureDate;
   private Date       receiveDate;
   private long       transitNumber;
   private int        checkNumber;
   private long       account;
   private BigDecimal amount;
   private String     cashierId;
   private ItemType   itemType;
   private ProcMethod procMethod;
   private String     batchId;
   private Date       settlementDate;
   private String     voucherNumber;
   private String[]   userFields = new String[NUM_USER_FIELDS];
   private String     agencyAcctCode;
   private String     description;

   public static enum ItemType   {
      PERSONAL, NON_PERSONAL
   };

   public static enum ProcMethod {
      CUSTOMER_NOT_PRESENT, CUSTOMER_PRESENT, BACK_OFFICE
   }

   public String getIrn() {
      return irn;
   }

   public void setIrn(String irn) {
      this.irn = irn;
   }

   public String getOtcEndpoint() {
      return otcEndpoint;
   }

   public void setOtcEndpoint(String otcEndpoint) {
      this.otcEndpoint = otcEndpoint;
   }

   public String getAlc2() {
      return alc2;
   }

   public void setAlc2(String alc2) {
      this.alc2 = alc2;
   }

   public Date getCaptureDate() {
      return captureDate;
   }

   public void setCaptureDate(Date captureDate) {
      this.captureDate = captureDate;
   }

   public Date getReceiveDate() {
      return receiveDate;
   }

   public void setReceiveDate(Date receiveDate) {
      this.receiveDate = receiveDate;
   }

   public long getTransitNumber() {
      return transitNumber;
   }

   public void setTransitNumber(long transitNumber) {
      this.transitNumber = transitNumber;
   }

   public int getCheckNumber() {
      return checkNumber;
   }

   public void setCheckNumber(int checkNumber) {
      this.checkNumber = checkNumber;
   }

   public long getAccount() {
      return account;
   }

   public void setAccount(long account) {
      this.account = account;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public String getCashierId() {
      return cashierId;
   }

   public void setCashierId(String cashierId) {
      this.cashierId = cashierId;
   }

   public ItemType getItemType() {
      return itemType;
   }

   public void setItemType(ItemType itemType) {
      this.itemType = itemType;
   }

   public ProcMethod getProcMethod() {
      return procMethod;
   }

   public void setProcMethod(ProcMethod procMethod) {
      this.procMethod = procMethod;
   }

   public String getBatchId() {
      return batchId;
   }

   public void setBatchId(String batchId) {
      this.batchId = batchId;
   }

   public Date getSettlementDate() {
      return settlementDate;
   }

   public void setSettlementDate(Date settlementDate) {
      this.settlementDate = settlementDate;
   }

   public String getVoucherNumber() {
      return voucherNumber;
   }

   public void setVoucherNumber(String voucherNumber) {
      this.voucherNumber = voucherNumber;
   }

   public String getUserField(int index) {
      return userFields[index];
   }

   public void setUserField(int index, String value) {
      userFields[index] = value;
   }

   public String getAgencyAcctCode() {
      return agencyAcctCode;
   }

   public void setAgencyAcctCode(String agencyAcctCode) {
      this.agencyAcctCode = agencyAcctCode;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   };
}
