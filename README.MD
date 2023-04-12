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

> 《移动App性能评测与优化》
> 
> 内存的主要组成索引：
> - Native Heap: Native代码分配的内存，虚拟机和Android框架本身也会分配
> - Dalvik Heap: Java代码分配的对象
> - Dalvik Other：类的数据结构和索引
> - so mmap: Native代码和常量
> - dex mmap: Java代码和常量
> 
> 内存工具：
> - Android Studio/Memory Monitor：观察Dalvik内存
> - dumpsys meminfo：观察整体内存
> - dumpsys procstats --hours 3
> - smaps：观察整体内存的详细组成
> - Eclipse Memory Analyzer：详细分析Dalvik内存
> 
> 电量工具
> - adb shell dumpsys batterystats
> - https://github.com/google/battery-historian