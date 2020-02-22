package com.ocdl.client.service.impl;

import com.ocdl.client.exception.LsException;
import com.ocdl.client.service.HttpRequestService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class DefaultHttpRequestService implements HttpRequestService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpRequestService.class);

    @Override
    public String post(String url, String body) {

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);

            // set post request
            post.setHeader("Content-type", "application/json");
            StringEntity bodyEntity = new StringEntity(body);
            post.setEntity(bodyEntity);

            // excute post request
            HttpResponse response = client.execute(post);
            logger.debug("\nSending 'POST' request to URL : " + url);
            logger.debug("Post parameters : " + post.getEntity());
            logger.debug("Response Code : " + response.getStatusLine().getStatusCode());

//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            StringBuffer resultBuff = new StringBuffer();
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                resultBuff.append(line);
//            }
//            logger.debug(resultBuff.toString());

            HttpEntity responseEntity = response.getEntity();
            String result = null;
            if(responseEntity!=null) {
                result = EntityUtils.toString(responseEntity);
            }

            return result;

        }catch (IOException e) {
            logger.debug("IOException: " + e);
            throw new LsException("Flask sever fail to deal with model: " + e);
        }

    }

}
