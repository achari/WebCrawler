package com.exmaple.web.crawler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.web.controller.MailArchiveDownloadController;
import com.exmaple.web.crawler.request.CrawlRequest;
import com.exmaple.web.crawler.request.MailArchiveResonse;
import com.exmaple.web.crawler.service.SimpleMailListingWebCrawler;
import com.flextrade.jfixture.JFixture;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MailArchiveDownloadControllerTests {

	@Configuration
	static class MailArchiveDownloadControllerTestsConfig {
		@Bean
		SimpleMailListingWebCrawler simpleMailListingWebCrawler() {
			return Mockito.mock(SimpleMailListingWebCrawler.class);
		}

		@Bean
		MailArchiveDownloadController mailArchiveDownloadController() {
			return new MailArchiveDownloadController();
		}
	}

	@Resource
	private SimpleMailListingWebCrawler simpleMailListingWebCrawler;

	@Resource
	private MailArchiveDownloadController sut;
	private MockMvc mockMvc;
	private JFixture jfFixture;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
		this.jfFixture = new JFixture();
	}

	@Test
	public void crawlAndDownloadArchives_ShouldReturnResponse_WhenPageCrawlsAndMatchedWithUrlPattern()
			throws Exception {
		when(simpleMailListingWebCrawler.getMailArchivesByCrawlRequest(any(CrawlRequest.class)))
				.thenReturn(jfFixture.collections().createCollection(List.class, MailArchiveResonse.class, 3));
		mockMvc.perform(get("/simpleCrawl/download/mailarchive/2017/zip")).andExpect(status().isOk());

		verify(simpleMailListingWebCrawler).getMailArchivesByCrawlRequest(any(CrawlRequest.class));
	}
}
