package org.example.functions.utils;

public enum FileNames {
  FIELD_1("Total # of Supplier Users"),
  FIELD_2("Total # of Internal users(w/o tech admins and Bus super users) "),
  FIELD_3("# of items by workflow type and statusOnboarding Completed"),
  FIELD_4("Riversand Initiated(Non-GDSN) "),
  FIELD_5("Riversand Initiated(GDSN Sync) "),
  FIELD_6("Take Action Initiated(GDSN Sync) "),
  FIELD_7("Migrated Items"),
  FIELD_8("Onboarding In Progress "),
  FIELD_9("1. Supplier enrichment"),
  FIELD_10("1. Merchant review and enrichment"),
  FIELD_11("1. Ecomm steps"),
  FIELD_12("2.Take Action Initiated(GDSN Sync)"),
  FIELD_13("2. Riversand initiated(GDSN Sync) "),
  FIELD_14("Onboarding Cancelled "),
  FIELD_15("Maintenance Completed "),
  FIELD_16("Updated by GDSN "),
  FIELD_17("Supplier Maintenance "),
  FIELD_18("Internal Maintenance "),
  FIELD_19("Items without GDSN Data "),
  FIELD_0("Items with GDSN Data ");

  private final String description;

  FileNames(final String description) {
    this.description = description;
  }

  public String description() {
    return description;
  }
}
