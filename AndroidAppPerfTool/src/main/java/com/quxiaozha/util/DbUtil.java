package com.quxiaozha.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DbUtil {
    public static Logger log = LoggerFactory.getLogger(DbUtil.class);
    private static String serverIP = "localhost";
    private static String port = "8086";
    private static String host;
    private static String query;
    private static String write;
    private static String dbName = "";

    public static void initialize() {
        host = "http://" + serverIP + ":" + port;
        query = host + "/query";
        write = host + "/write?db=";

        dbName = "db_" + ConfigUtil.getUdid();
        createDB(dbName);
        log.info("influx db host is " + host);
    }

    public static void createDB(String name) {
        log.info("Creating database " + name);
        sendPost(query, "q=CREATE DATABASE " + name);
        dbName = name;
    }

    public static void writeData(String data) {
        sendPost(write + dbName, data);
    }

    public static void sendPost(String url, String data) {
//        log.info("data: " + data);
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);
            con.setDoInput(true);

            PrintWriter printWriter = new PrintWriter(con.getOutputStream());
            printWriter.write(data);
            printWriter.flush();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.info("Sending data to db: " + url + ", Response Code: " + responseCode + ", Response Body: " + response);
        } catch (Exception e) {
            log.error("Fail to send request to DB");
            e.printStackTrace();
        }
    }

    public static void writeDataToDB(String tableName) {
        Long timeStamp = System.currentTimeMillis() * 1000000L;
        Map<String, String> memInfo = AdbUtil.getMemInfo();
        if (memInfo.isEmpty()) {
            log.error("Can not get the memory info, skip...");
            return;
        }
        String cpuInfo = AdbUtil.getCPUInfo();
        if (cpuInfo.isEmpty()) {
            log.error("Can not get cpu info, skip...");
            return;
        }
        memInfo.put("cpu", cpuInfo);
        StringBuilder dbRecord = new StringBuilder();

        for (String key : memInfo.keySet()) {
            dbRecord.append(key.replace(" ", "_"));
            dbRecord.append("=");
            dbRecord.append(memInfo.get(key));
            dbRecord.append(",");
        }
        dbRecord.append(timeStamp);
        String record = dbRecord.toString();
        record = record.replace("," + timeStamp, " " + timeStamp);
        record = tableName + " " + record;

        log.info("Perf data: " + record);
        DbUtil.writeData(record);
    }
}
