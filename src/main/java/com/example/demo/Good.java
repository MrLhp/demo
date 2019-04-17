package com.example.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Good extends Thread{

    private int index;

    private List<String> list;

    public Good(int index, List<String> list){
        this.index = index;
        this.list = list;
    }

    public static void price(int index, List<String> fileList) {
        String url = "https://gjdyzjb.cn/srfs/w/searchTicketFromHBase/s?page=0&size=1000&s_cinemaCode=REPLACE_CINEMACODE&s_sessionCode=REPLACE_SESSION&s_cinemaId=REPLACE_CINEMAID";
        Cache<String, CinemaInfo> cinemaCache = CacheInstance.getCache();
        List<ExcelData> returnList = new ArrayList<>();
        HttpPost post = new HttpPost();
        try {
            for (int i = 0; i < fileList.size(); i++) {
                String queryUrl;
                String line = fileList.get(i);
                String[] filed = line.split(",");

                //获取影院信息
                CinemaInfo cinemaInfo = cinemaCache.getIfPresent(filed[0]);
                if(cinemaInfo == null){
                    cinemaInfo = CinemaInfoQuery.queryCinemaInfo(filed[0]);
                    cinemaCache.put(filed[0], cinemaInfo);
                }
                if(cinemaInfo == null){
                    log.info("影院[ {} ]查询不存在!!!", filed[0]);
                    continue;
                }

                queryUrl = url.replace("REPLACE_CINEMACODE", cinemaInfo.getCinemaCode()).replace("REPLACE_SESSION", StringUtils.leftPad(filed[1], 16, "0")).replace("REPLACE_CINEMAID", cinemaInfo.getCinemaId().toString()).trim();
                post.setURI(URI.create(queryUrl));
                CloseableHttpResponse response = KeepSession.getHttpClient().execute(post, KeepSession.getHttpClientContext());
                String resString = EntityUtils.toString(response.getEntity());

                List<ExcelData> excelData = jsonParse(resString);
                returnList.addAll(excelData);
                log.info("影院编码[{}]-------场次编码[{}]数据查询解析完毕", filed[0], filed[1]);
                Thread.sleep(50);
            }

            ExcelExport.exportExcel(returnList, index);
            log.info("数据查询导出成功,数据为:{}----{}", index, index + fileList.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<ExcelData> jsonParse(String jsonStr) {
        Cache<String, CinemaInfo> cache = CacheInstance.getCache();
        List<ExcelData> excelDataList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(jsonStr);
        JsonArray dataArray = object.get("data").getAsJsonArray();
        if (dataArray != null && dataArray.size() > 0) {
            for (int i = 0; i < dataArray.size(); i++) {
                try {
                    JsonObject subObject = dataArray.get(i).getAsJsonObject();
                    ExcelData excelData = new ExcelData();
                    String cinemaCode = subObject.get("cinemaCode") == null ? "" : subObject.get("cinemaCode").getAsString();
                    if (cinemaCode != null) {
                        if (cache.getIfPresent(cinemaCode) != null) {
                            CinemaInfo cinemaInfo = CacheInstance.getCache().getIfPresent(cinemaCode);
                            excelData.setCinemaCode(cinemaCode);
                            excelData.setCinemaName(cinemaInfo.getCinemaName());
                            excelData.setCinemaChainName(cinemaInfo.getCinemaChainName());
                            excelData.setProvinceName(cinemaInfo.getProvinceName());
                        }else{
                            CinemaInfo cinemaInfo = CinemaInfoQuery.queryCinemaInfo(cinemaCode);
                            cache.put(cinemaCode, cinemaInfo);
                            excelData.setCinemaCode(cinemaCode);
                            excelData.setCinemaName(cinemaInfo.getCinemaName());
                            excelData.setCinemaChainName(cinemaInfo.getCinemaChainName());
                            excelData.setProvinceName(cinemaInfo.getProvinceName());
                        }
                    }
                    excelData.setOperation(subObject.get("operation") == null ? "" : operationChange(subObject.get("operation").getAsInt()));
                    excelData.setSessionCode(subObject.get("sessionCode") == null ? "" : subObject.get("sessionCode").getAsString());
                    excelData.setSessionTime(subObject.get("sessionDatetime") == null ? "" : subObject.get("sessionDatetime").getAsString());
                    excelData.setBusinessDate(subObject.get("businessDate") == null ? "" : subObject.get("businessDate").getAsString());
                    excelData.setPrice(subObject.get("price") == null ? 0l : subObject.get("price").getAsDouble());
                    excelData.setService(subObject.get("service") == null ? 0l : subObject.get("service").getAsDouble());
                    excelDataList.add(excelData);
                } catch (RuntimeException e){
                    log.info("数据解析失败: [ {} ]", jsonStr);
                    e.printStackTrace();
                }
            }
        }
        return excelDataList;
    }

    public static String operationChange(int i){
        String operation = "";
        switch (i) {
            case 1:
                operation = "售票";
                break;
            case 2:
                operation = "退票";
                break;
            case 3:
                operation = "预售";
                break;
            case 4:
                operation = "补登";
                break;
            default:
        }
        return operation;
    }

    @Override
    public void run() {
        Good.price(this.index, this.list);
    }
}
