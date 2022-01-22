package com.example.natwestspendingtracker.enums;

public enum AppColor {
    RED("#F1433F"),
    YELLOW("#F7E967"),
    GREEN("#A9CF54");

    final String hex;

    AppColor(String hex) {
        this.hex = hex;
    }

    public String getHex(){
        return this.hex;
    }
}
