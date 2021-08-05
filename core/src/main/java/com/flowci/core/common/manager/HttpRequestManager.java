package com.flowci.core.common.manager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Locale;

@Component("httpManager")
public class HttpRequestManager {

    private final static int DefaultTimeout = 30 * 1000;

    private final static String DefaultUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36";

    private HttpClient client;

    @PostConstruct
    public void init() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(DefaultTimeout)
                .setConnectionRequestTimeout(DefaultTimeout)
                .setSocketTimeout(DefaultTimeout)
                .build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public String get(String url) throws IOException {
        return get(url, null);
    }

    public String get(String url, String token) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", DefaultUserAgent);

        if(!StringUtils.isEmpty(token)) {
            request.setHeader("Authorization", "Bearer " + token.trim());
        }

        HttpResponse execute = client.execute(request);
        return EntityUtils.toString(execute.getEntity());
    }
}
