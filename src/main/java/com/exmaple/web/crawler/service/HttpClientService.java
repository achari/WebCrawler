package com.exmaple.web.crawler.service;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

@Service
public class HttpClientService {

	private HttpClient getHttpClient() {
		return HttpClientBuilder.create().build();
	}

	public byte[] executeGetRequest(String url) throws ClientProtocolException, IOException {
		HttpGet getRequest = new HttpGet(url);

		// set headers if needed
		HttpResponse response = getHttpClient().execute(getRequest);

		return IOUtils.toByteArray(response.getEntity().getContent());
	}

}
