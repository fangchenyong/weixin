package entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Article {
	@XStreamAlias("Title")
	private String title;
	@XStreamAlias("Discription")
	private String discription;
	@XStreamAlias("PicUrl")
	private String picUrl;
	@XStreamAlias("Url")
	private String url;
	protected String getTitle() {
		return title;
	}
	protected void setTitle(String title) {
		this.title = title;
	}
	protected String getDiscription() {
		return discription;
	}
	protected void setDiscription(String discription) {
		this.discription = discription;
	}
	protected String getPicUrl() {
		return picUrl;
	}
	protected void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	protected String getUrl() {
		return url;
	}
	protected void setUrl(String url) {
		this.url = url;
	}
	public Article(String title, String discription, String picUrl, String url) {
		super();
		this.title = title;
		this.discription = discription;
		this.picUrl = picUrl;
		this.url = url;
	}
	
	
}
