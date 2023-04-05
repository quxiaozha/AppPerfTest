package com.quxiaozha.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AdbUtil {
    public static Logger log = LoggerFactory.getLogger(AdbUtil.class);
    private static final String nativeKey = "Native Heap";
    private static final String dalKey = "Dalvik Heap";
    private static final String totalKey = "TOTAL";

    public static String exeCmd(String commandStr) {
        log.info("Method exeCmd : " + commandStr);

        String[] commandStrArr = commandStr.split(" ");
        BufferedReader br = null;
        String res = "";

        try {
            ProcessBuilder pb = new ProcessBuilder(commandStrArr);
            Process pc = pb.start();
            br = new BufferedReader(new InputStreamReader(pc.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }

            res = output.toString().trim();
            pc.waitFor();
            pc.destroy();
        } catch (Exception e) {
            log.error(commandStr + "fail to execute : " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

    public static Map<String, String> getMemInfo() {
        Map<String, String> map = new HashMap<>();
        String res = AdbUtil.exeCmd("adb -s " + ConfigUtil.getUdid() + " shell COLUMNS=512 dumpsys meminfo " + ConfigUtil.getPackageName());

        if (res.contains("No process found for")) {
            return map;
        }

        String nativeMem = getMemString(res, nativeKey);
        String dalvikMem = getMemString(res, dalKey);
        String totalMem = getMemString(res, totalKey);

        map.put(nativeKey, nativeMem);
        map.put(dalKey, dalvikMem);
        map.put(totalKey, totalMem);

        return map;
    }

    public static String getMemString(String output, String key) {
        int index = output.indexOf(key) + key.length();
        return output.substring(index, index + 9).trim();
    }

    public static String getCPUInfo() {
        String cmd = "adb -s " + ConfigUtil.getUdid() + " shell COLUMNS=512 top -n 1 | " + ConfigUtil.getGrep() + " " + ConfigUtil.getPackageName();
        String res = AdbUtil.exeCmd(cmd);
        if (res.isEmpty()) {
            return "";
        }

        //may contain more than 1 record, need add all
        String[] cpuRecords = res.trim().split("\n");
        BigDecimal sumCpu = new BigDecimal(0);

        for (String cpuRecord : cpuRecords) {
            if(StringUtils.isBlank(cpuRecord)){
                continue;
            }

            String[] val = cpuRecord.split(" ");
            int CPU_INDEX = 9;
            int index = 0;
            if(val.length < 9){
                continue;
            }
            for (int i = 0; i < val.length; i++) {
                if (!val[i].isEmpty()) {
                    index++;
                }
                if (index == CPU_INDEX) {
                    index = i;
                    break;
                }
            }

            Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?");
            if (pattern.matcher(val[index]).matches()) {
                BigDecimal curCpu = new BigDecimal(val[index]);
                sumCpu = sumCpu.add(curCpu);
            } else {
                return "";
            }
        }
        return String.valueOf(sumCpu);
    }
}
