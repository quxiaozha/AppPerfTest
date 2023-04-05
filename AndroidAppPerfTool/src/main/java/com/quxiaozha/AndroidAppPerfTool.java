package com.quxiaozha;

import com.quxiaozha.util.ConfigUtil;
import com.quxiaozha.util.DbUtil;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AndroidAppPerfTool {
    public static Logger log = LoggerFactory.getLogger(AndroidAppPerfTool.class);
    public static void main(String[] args) throws InterruptedException, ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("u", "udid", true, "device serial number");
        options.addOption("p", "package", true, "Android package name");
        options.addOption("d", "duration", true, "fetch performance data duration");
        CommandLine commandLine = parser.parse(options, args);
        if(!commandLine.hasOption("u") || !commandLine.hasOption("p")){
            log.error("Please provide udid with -u and package name with -p");
            return;
        }
        int duration = 2000;
        if(commandLine.hasOption("d")){
            duration = Integer.parseInt(commandLine.getOptionValue("d"))*1000;
        }
        ConfigUtil.initialize(commandLine.getOptionValue("u"), commandLine.getOptionValue("p"));
        DbUtil.initialize();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = sdf.format(new Date());
        String tableName = "tb_" + time;
        while (true) {
            DbUtil.writeDataToDB(tableName);
            Thread.sleep(duration);
        }
    }
}
