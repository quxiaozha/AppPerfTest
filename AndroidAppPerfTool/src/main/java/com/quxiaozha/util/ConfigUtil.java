package com.quxiaozha.util;

public class ConfigUtil {
    private static ConfigUtil configUtil = new ConfigUtil();

    private String packageName;
    private String udid;
    private String grep = "grep";

    private ConfigUtil() {
    }

    public static void initialize(String udid, String packageName) {
        configUtil.setUdid(udid);
        configUtil.setPackageName(packageName);
    }

    private void setPackageName(String packageName) {
        configUtil.packageName = packageName;
    }

    private void setUdid(String udid) {
        configUtil.udid = udid;
    }

    private void setGrep(String grep) {
        configUtil.grep = grep;
    }

    public static String getPackageName() {
        return configUtil.packageName;
    }

    public static String getUdid() {
        return configUtil.udid;
    }

    public static String getGrep() {
        return configUtil.grep;
    }

}
