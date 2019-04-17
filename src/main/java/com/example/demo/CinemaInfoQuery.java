package com.example.demo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class CinemaInfoQuery {

    public static final String QUERY_URL = "https://gjdyzjb.cn/bits/w/cinemas/s?page=0&size=50&s_cinemaCode=S_CINEMA_CODE&s_showHistory=false&sort=id,desc";

    public static CinemaInfo queryCinemaInfo(String cinemaCode) {
        HttpPost post = new HttpPost();
        try {
            String queryUrl = QUERY_URL.replace("S_CINEMA_CODE", cinemaCode);
            post.setURI(URI.create(queryUrl));
            CloseableHttpResponse response = KeepSession.getHttpClient().execute(post, KeepSession.getHttpClientContext());
            String resString = EntityUtils.toString(response.getEntity());
            //数据解析
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(resString);
            JsonArray dataArray = object.get("data").getAsJsonArray();
            if(dataArray != null && dataArray.size() > 0){
                JsonObject subObject = dataArray.get(0).getAsJsonObject();
                CinemaInfo cinemaInfo = new CinemaInfo();
                cinemaInfo.setCinemaId(subObject.get("cinemaId").getAsLong());
                cinemaInfo.setCinemaCode(subObject.get("cinemaCode").getAsString());
                cinemaInfo.setCinemaName(subObject.get("shortName").getAsString());
                cinemaInfo.setCinemaChainName(subObject.get("cinemaChainName").getAsString());
                cinemaInfo.setProvinceName(subObject.get("provinceName").getAsString());
                return cinemaInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
