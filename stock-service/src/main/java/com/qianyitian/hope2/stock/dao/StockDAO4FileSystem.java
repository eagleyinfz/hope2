package com.qianyitian.hope2.stock.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import com.qianyitian.hope2.stock.config.Constant;
import com.qianyitian.hope2.stock.config.EStockKlineType;
import com.qianyitian.hope2.stock.config.PropertyConfig;
import com.qianyitian.hope2.stock.model.KLineInfo;
import com.qianyitian.hope2.stock.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import static com.qianyitian.hope2.stock.config.Constant.LITE_LEAST_DAY_NUMBER;


@Repository("stockDAO4FileSystem")
public class StockDAO4FileSystem extends AbstractStockDAO {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    PropertyConfig propertyConfig;

    public StockDAO4FileSystem() {
    }


    private String getRootPath() {
        return propertyConfig.getDataPath();
    }

    @Override
    public void storeAllSymbols(List<Stock> list) {
        File rootFolder = new File(getRootPath());
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        String allSymbols = JSON.toJSONString(list);
        File to = new File(getRootPath(), "allSymbols");
        try {
            Files.asCharSink(to, Charset.forName("UTF-8")).write(allSymbols);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void storeStockInfo(Stock stock, EStockKlineType type) {
        String jsonStock = JSON.toJSONString(stock);
        File parent = new File(getRootPath(), type.getFolderName());
        File to = new File(parent, stock.getCode() + type.getFileSuffix());
        try {
            Files.asCharSink(to, Charset.forName("UTF-8")).write(jsonStock);
        } catch (IOException e) {
            logger.error("store stock error on " + stock.getCode(), e);
        }
    }


    @Override
    public Stock getStockInfo(String code, EStockKlineType type) {
        File parent = new File(getRootPath(), type.getFolderName());
        File from = new File(parent, code + type.getFileSuffix());
        String rs;
        try {
            rs = Files.asCharSource(from, Charset.forName("UTF-8")).readFirstLine();
            Stock stock = JSON.parseObject(rs.toString(), Stock.class);
            return stock;
        } catch (IOException e) {
            logger.error("get stock error on " + code, e);
        }
        return null;
    }

    @Override
    public List<Stock> getAllSymbols() {
        File rootFolder = new File(getRootPath());
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        File from = new File(getRootPath(), "allSymbols");
        if (!from.exists()) {
            return null;
        }
        String rs;
        try {
            rs = Files.asCharSource(from, Charset.forName("UTF-8")).readFirstLine();
            List<Stock> stocks = JSON.parseArray(rs.toString(), Stock.class);
            return stocks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Stock> getFavoriteSymbols() {
        File rootFolder = new File(getRootPath());
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        File from = new File(getRootPath(), "favoriteSymbols");
        if (!from.exists()) {
            return null;
        }
        String rs;
        try {
            rs = Files.asCharSource(from, Charset.forName("UTF-8")).readFirstLine();
            List<Stock> stocks = JSON.parseArray(rs.toString(), Stock.class);
            return stocks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Date getStockUpdateTime(String code) {
        File file = new File(getRootPath(), code);
        long time = file.lastModified();
        Date lastDate = new Date(time);
        return lastDate;
    }


    @Override
    public Date getAllSymbolsUpdateTime() {
        File file = new File(getRootPath(), "allSymbols");
        long time = file.lastModified();
        Date lastDate = new Date(time);
        return lastDate;
    }
}
