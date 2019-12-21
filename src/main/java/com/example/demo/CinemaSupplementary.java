package com.example.demo;

import com.example.demo.utils.JsonUtils;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 影院补登审核
 */
@Slf4j
public class CinemaSupplementary {
    private String pending="pending";
    private String waiting="waiting";
    public String QUERY_URL = "https://gjdyzjb.cn/boms/w/cinemaTicketSupplementApplications/s?page=REPLACE_PAGE&size=100&" +
            "s_createdDateStrat=1546272000000&s_createdDateEnd=1577807999999&s_sessionDateTimeStrat=1546272000000&s_sessionDateTimeEnd=1577807999999" +
            "&s_result=" +
            waiting+"&sort=credentials,id,desc";
    private int pages = 0;
    private AtomicInteger count = new AtomicInteger(0);
    private long price = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //File file = new File("d:\\cinema_supplement.txt");
    //BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
    //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"),5 * 1024 * 1024);
    //
    //File file_log = new File("d:\\cinema_supplement_log.txt");
    //BufferedOutputStream fos_log = new BufferedOutputStream(new FileOutputStream(file_log));
    //BufferedWriter writer_log = new BufferedWriter(new OutputStreamWriter(fos_log,"utf-8"),5 * 1024 * 1024);

    FileWriter writer=new FileWriter("d:\\cinema_supplement.txt", true);
    FileWriter writer_log=new FileWriter("d:\\cinema_supplement_log.txt", true);

    public CinemaSupplementary() throws IOException {
    }

    public void supplementary(CloseableHttpClient httpClient, HttpClientContext httpClientContext, HttpPost post){
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(post, httpClientContext);

            //获取分页及内容信息
            String content = EntityUtils.toString(httpResponse.getEntity());
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(content);
            JsonArray dataArray = object.get("data").getAsJsonArray();
            JsonObject pageable = object.get("pageable").getAsJsonObject();

            if (dataArray!=null && dataArray.size()>0) {
                log.info("共[{}]页",pageable.get("totalPages").getAsInt());
                for (int i = 0; i < dataArray.size(); i++) {

                    JsonObject data = dataArray.get(i).getAsJsonObject();
                    CinemaSupplementBean cinemaSupplementBean = new Gson().fromJson(data, CinemaSupplementBean.class);
                    String pass_url = "https://gjdyzjb.cn/boms/w/cinemaTicketSupplementApplications/approval/"+cinemaSupplementBean.getId()+"/administrator/pass";
                    HttpGet httpGet = new HttpGet();
                    httpGet.setURI(URI.create(pass_url));
                    CloseableHttpResponse response = httpClient.execute(httpGet, httpClientContext);
                    writer.flush();
                    writer.write(cinemaSupplementBean.toString());
                    writer.write(System.getProperty("line.separator"));

                    writer_log.flush();
                    StringBuilder sb = new StringBuilder();
                    sb
                            .append(cinemaSupplementBean.getCinemaCode()).append("#")
                            .append(sdf.format(cinemaSupplementBean.getBusinessDate())).append("#")
                            .append(cinemaSupplementBean.getScreenCode()).append("#")
                            .append(cinemaSupplementBean.getSessionCode());
                    String s = EntityUtils.toString(response.getEntity());
                    writer_log.write(sb.toString()+"\t"+s);
                    writer_log.write(System.getProperty("line.separator"));

                    price+=cinemaSupplementBean.getPrice();
                    count.incrementAndGet();
                    Thread.sleep(RandomUtils.nextInt(10,50));

                }

                log.info("一共处理{}条数据，共计{}元",count,price);
                //获取下一页
                String queryUrl=QUERY_URL.replace("REPLACE_PAGE",String.valueOf(pages));
                post.setURI(URI.create(queryUrl));
                supplementary(httpClient,httpClientContext,post);

            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
