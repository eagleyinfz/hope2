# hope2

hope的改进版，删除了spring cloud的依赖，网络调用直接配置在各个spring boot里面。

```
hope2:
  service:
    spider: http://127.0.0.1:8001
    stock: http://127.0.0.1:8002
    analyzer: http://127.0.0.1:8003
```

直接启动
```
SpiderApplication
StockApplication
AnalyzerApplication
```


开始运行爬虫
```
http://127.0.0.1:8001/force_refresh_stocks
```

查看分析报告
```
http://127.0.0.1:8003/
```

股票数据默认存储在磁盘,位置是 ./data/
为了便于部署，去掉了对于数据库和redis的依赖，只要有硬盘就可以跑起来,磁盘占用2G。
数据库和redis的实现也提供了，需要的话，代码配置下就可以用了。
更新， 数据库和redis看着烦，给彻底删除了

Demark算法的访问方式
http://127.0.0.1:8003/demark?days2Now=500
days2Now=500 表示需要计算距今多久的数据，不输入的话，系统默认值300天
该算法只计算指定的股票，股票列表在文件./data/favoriteSymbols 中手工维护。


运行mvn package 后，在hope2/target/目录下生成jar文件
将jar文件和data目录部署在同一位置。
运行如下命令启动
```
nohup java -jar -Dspring.profiles.active=prod stock-service-1.0.jar >> ./log/stock.log 2>&1 &
nohup java -jar -Dspring.profiles.active=prod spider-service-1.0.jar >> ./log/spider.log 2>&1 &
nohup java -jar -Dspring.profiles.active=prod analyzer-service-1.0.jar >> analyzer.log 2>&1 &
```

nohup java -jar -Dspring.profiles.active=prod -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8004  analyzer-service-1.0.jar >> analyzer.log 2>&1 &

-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 