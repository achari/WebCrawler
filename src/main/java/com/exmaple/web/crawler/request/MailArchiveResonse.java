package com.exmaple.web.crawler.request;

public class MailArchiveResonse {

	private String fileName;
	private byte[] content;

	/**
	 * @param fileName
	 * @param content
	 */
	public MailArchiveResonse(String fileName, byte[] content) {
		super();
		this.fileName = fileName;
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
