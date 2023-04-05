## A simple Android App performance testing tool based on adb+InfluxDB+Grafana
##### It hasn't been verified and need enhance to make the data more accurate
[![Java CI with Maven](https://github.com/quxiaozha/AppPerfTest/actions/workflows/maven.yml/badge.svg)](https://github.com/quxiaozha/AppPerfTest/actions/workflows/maven.yml)

![AndroidAppPerfTool.png](screenshot%2FAndroidAppPerfTool.png)

- Clone the project
- cd /AndroidAppPerfTool
- Run docker-compose up
- Run java -jar AndroidAppPerfTool-0.1-jar-with-dependencies.jar -u [device_uuid] -p [package_name]
- Open http://127.0.0.1:3000/login to configure the influxdb data source
- Add the dashboard you want and play with it ^_^
