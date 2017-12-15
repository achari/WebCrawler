package com.exmaple.web.crawler.request;

public class CrawlRequest {

	private String baseUrl;
	private String regx;
	private CrawlRequest childRequest;
	/**
	 * @param baseUrl
	 * @param regx
	 */
	public CrawlRequest(String baseUrl, String regx) {
		super();
		this.baseUrl = baseUrl;
		this.regx = regx;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getRegx() {
		return regx;
	}
	public void setRegx(String regx) {
		this.regx = regx;
	}
	public CrawlRequest getChildRequest() {
		return childRequest;
	}
	public void setChildRequest(CrawlRequest childRequest) {
		this.childRequest = childRequest;
	}
	
}
