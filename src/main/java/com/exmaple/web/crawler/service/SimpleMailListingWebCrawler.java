package com.exmaple.web.crawler.service;

import java.io.IOException;
import java.util.List;

import com.exmaple.web.crawler.request.CrawlRequest;
import com.exmaple.web.crawler.request.MailArchiveResonse;

public interface SimpleMailListingWebCrawler {

	List<MailArchiveResonse> getMailArchivesByCrawlRequest(CrawlRequest crawlRequest) throws IOException,InterruptedException;
}
