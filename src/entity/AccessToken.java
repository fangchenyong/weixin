package entity;

public class AccessToken {

	public String accessToken;
	public long expireTime;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public AccessToken(String accessToken, String expireIn) {
		super();
		this.accessToken = accessToken;
		expireTime = System.currentTimeMillis()+Integer.valueOf(expireIn)*1000;
	}
	
	public boolean isExpired(){
		return System.currentTimeMillis()>expireTime;
	}
}
