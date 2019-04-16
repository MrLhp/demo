package com.example.demo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;
@Slf4j
public class Good {

    private static double sumPrice;
    public static void price() {
        List<String> fileList = FileDataReader.getFileContentArrary("d:\\tmp\\32029151.csv");
        String url = "https://gjdyzjb.cn/srfs/w/searchTicketFromHBase/s?page=0&size=500&s_cinemaCode=32029151&s_sessionCode=REPLACE_SESSION&s_ticketNo=REPLACE_TICKET&s_cinemaId=6860&s_name=%E6%B1%9F%E8%8B%8F%E6%98%86%E5%B1%B1%E4%B8%AD%E5%BD%B1%E4%B9%9D%E6%96%B9%E5%BD%B1%E5%9F%8E&";
        HttpPost post = new HttpPost();
        try {
            for (int i = 0; i < fileList.size(); i++) {
                String queryUrl;
                String line = fileList.get(i);
                String[] filed = line.split(",");
                queryUrl = url.replace("REPLACE_SESSION", filed[8]).replace("REPLACE_TICKET", filed[10]);
                post.setURI(URI.create(queryUrl));
                CloseableHttpResponse response = KeepSession.getHttpClient().execute(post, KeepSession.getHttpClientContext());
                String resString = EntityUtils.toString(response.getEntity());

                jsonParse(resString, Double.parseDouble(filed[13]),filed,i);

                Thread.sleep(50);
            }
            Good.log.info("sumprice【{}】",sumPrice);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void jsonParse(String jsonStr, double csvPrice, String[] filed,int j) {

        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(jsonStr);
        JsonArray dataArray = object.get("data").getAsJsonArray();
        if (dataArray != null && dataArray.size() > 0) {
            for (int i = 0; i < dataArray.size(); i++) {
                JsonObject subObject = dataArray.get(i).getAsJsonObject();
                double hbasePrice = subObject.get("price").getAsDouble();
                if (subObject.get("operation").getAsInt()==2) {
                    hbasePrice=-hbasePrice;
                }

                sumPrice+=hbasePrice;

                if (csvPrice!=hbasePrice) {
                    Good.log.info(subObject.toString());
                    Good.log.info("第【{}】次查询完毕 csv【{}】  HBASE【{}】 sessioncode【{}】 ticketcode【{}】",j,csvPrice,hbasePrice,filed[8],filed[10]);
                }else{
                    Good.log.info("第【{}】次查询完毕",j);
                }
            }
        }
    }

}
