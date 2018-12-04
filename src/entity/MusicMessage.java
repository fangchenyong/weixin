package entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("xml")
public class MusicMessage extends BaseMessge {

	private Music music;

	protected Music getMusic() {
		return music;
	}

	protected void setMusic(Music music) {
		this.music = music;
	}
	
	public MusicMessage(Map<String, String> requestMap,Music music) {
		super(requestMap);
		setMsgType("music");
		this.music=music;
	}
}
