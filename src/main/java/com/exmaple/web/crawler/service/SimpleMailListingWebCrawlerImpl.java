package com.exmaple.web.crawler.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.exmaple.web.crawler.request.CrawlRequest;
import com.exmaple.web.crawler.request.MailArchiveResonse;

@Service
public class SimpleMailListingWebCrawlerImpl implements SimpleMailListingWebCrawler {

	@Resource
	private HttpClientService httpClientService;
	private static final Logger log = Logger.getLogger(SimpleMailListingWebCrawlerImpl.class);
	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public List<MailArchiveResonse> getMailArchivesByCrawlRequest(CrawlRequest crawlRequest)
			throws IOException, InterruptedException {

		if (crawlRequest == null)
			return Collections.emptyList();

		Set<String> mailArchiveLinks = getPageLinks(crawlRequest.getBaseUrl(), crawlRequest.getRegx());
		List<MailArchiveResonse> archives = new ArrayList<>();
		List<Future<MailArchiveResonse>> futures = new ArrayList<>();

		mailArchiveLinks.parallelStream().forEach(link -> {
			try {
				futures.add(getMailArchiveResponse(link));
			} catch (IOException e) {
				log.error("Exception while submiting the Future", e);
			}
		});

		futures.forEach(f -> {
			try {
				archives.add(f.get());
			} catch (InterruptedException | ExecutionException e) {
				log.error("Exception while executing the feature", e);
			}
		});

		return archives;
	}

	private Future<MailArchiveResonse> getMailArchiveResponse(String link) throws ClientProtocolException, IOException {
		log.info("processing link = " + link);
		return executorService.submit(new Callable<MailArchiveResonse>() {
			@Override
			public MailArchiveResonse call() throws Exception {
				byte[] content = httpClientService.executeGetRequest(link);
				return new MailArchiveResonse(link.substring(link.lastIndexOf("/")), content);
			}
		});

	}

	public Set<String> getPageLinks(String baseUrl, String regx) throws IOException {
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Document document = Jsoup.connect(baseUrl).get();
		return document.select("a[href]").stream().filter(page -> pattern.matcher(page.attr("href")).find())
				.map(page -> page.attr("abs:href").substring(0, page.attr("abs:href").lastIndexOf("/"))).sorted()
				.collect(Collectors.toSet());

	}
}
