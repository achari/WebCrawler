package com.exmaple.web.crawler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.exmaple.web.crawler.request.CrawlRequest;
import com.exmaple.web.crawler.request.MailArchiveResonse;
import com.exmaple.web.crawler.service.HttpClientService;
import com.exmaple.web.crawler.service.SimpleMailListingWebCrawler;
import com.exmaple.web.crawler.service.SimpleMailListingWebCrawlerImpl;
import com.flextrade.jfixture.JFixture;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
// @PrepareForTest(Jsoup.class)
public class SimpleMailListingWebCrawlerImplTests {

	@Configuration
	static class SimpleMailListingWebCrawlerImplTestsConfig {

		@Bean
		public HttpClientService httpClientService() {
			return Mockito.mock(HttpClientService.class);
		}

		@Bean
		public SimpleMailListingWebCrawler simpleMailListingWebCrawler() {
			return new SimpleMailListingWebCrawlerImpl();
		}
	}

	@Resource
	private HttpClientService httpClientService;

	@Resource
	private SimpleMailListingWebCrawler sut;

	private JFixture jFixture;
	private Document document;

	public SimpleMailListingWebCrawlerImplTests() {
		jFixture = new JFixture();
	}

	@Before
	public void setup() throws IOException {
		// PowerMockito.mockStatic(Jsoup.class);
		// document = jFixture.create(Document.class);
		// when(Jsoup.connect(anyString()).get()).thenReturn(document);
	}

	@Test
	public void getMailArchivesByCrawlRequest_ShouldReturnListOfMailArchiveResonse_WhenCrawls()
			throws ClientProtocolException, IOException, InterruptedException {
		// document = spy(document);
		CrawlRequest request = new CrawlRequest("http://mail-archives.apache.org/mod_mbox/maven-users/",
				"(year[0-9]*\\.mbox)".replace("year", "2016"));
		when(httpClientService.executeGetRequest(anyString())).thenReturn(jFixture.create(byte[].class));
		List<MailArchiveResonse> result = sut.getMailArchivesByCrawlRequest(request);

		assertNotNull(result);
		assertTrue(CollectionUtils.isNotEmpty(result));
	}
}
