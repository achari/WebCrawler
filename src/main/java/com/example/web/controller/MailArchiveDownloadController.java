package com.example.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exmaple.web.crawler.request.CrawlRequest;
import com.exmaple.web.crawler.request.MailArchiveResonse;
import com.exmaple.web.crawler.service.SimpleMailListingWebCrawler;

@RestController
@RequestMapping("/simpleCrawl")
public class MailArchiveDownloadController {

	private static final Logger log = Logger.getLogger(MailArchiveDownloadController.class);

	@Resource
	private SimpleMailListingWebCrawler simpleMailListingWebCrawler;

	@RequestMapping(value = "/download/mailarchive/{year}/zip", produces = "application/zip")
	public void crawlAndDownloadArchives(@PathVariable("year") String year, HttpServletResponse response) throws IOException, InterruptedException {

		if (year == null) {
			response.getOutputStream().write("Invalid Year".getBytes());
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition", "attachment; filename=\"MailArchives.zip\"");

		CrawlRequest crawlRequest = new CrawlRequest("http://mail-archives.apache.org/mod_mbox/maven-users/",
				"(year[0-9]*\\.mbox)".replace("year", year));
		List<MailArchiveResonse> archives = simpleMailListingWebCrawler.getMailArchivesByCrawlRequest(crawlRequest);
		ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

		if (CollectionUtils.isNotEmpty(archives)) {

			archives.forEach(archive -> {
				try {
					zos.putNextEntry(new ZipEntry(archive.getFileName()));
					zos.write(archive.getContent());
					zos.closeEntry();
				} catch (IOException e) {
					log.error("Excption whil downloading archives", e);
				}
			});
		}
		zos.close();
	}
	
	@RequestMapping("/test")
	public String getSmaple(){
		return "Helloo...";
	}
}
