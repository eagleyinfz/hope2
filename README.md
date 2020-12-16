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
查看报告需要配置redis.

Demark算法的访问方式
http://127.0.0.1:8003/demark?days2Now=500
days2Now=500 表示需要计算距今多久的数据，不输入的话，系统默认值300天
该算法只计算指定的股票，股票列表在文件./data/favoriteSymbols 中手工维护。