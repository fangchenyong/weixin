package test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.baidu.aip.ocr.AipOcr;
import com.thoughtworks.xstream.XStream;

import Service.WxService;
import entity.Button;
import entity.ClickButton;
import entity.ImageMessage;
import entity.MusicMessage;
import entity.NewsMessage;
import entity.PhotoOrAblumButton;
import entity.SubButton;
import entity.TextMessage;
import entity.VideoMessage;
import entity.ViewButton;
import entity.VoiceMessage;
import net.sf.json.JSONObject;
import util.Util;


public class TestWx {
	public static final String APP_ID = "15026081";
    public static final String API_KEY = "WrVkDdnWP2Ng2tvVhi25nrDp";
    public static final String SECRET_KEY = "WC1Vi7DNVrkUV5GYz3AOVYBoiyOyYR3n";

    @Test
    public void testGetUserInfo() {
    	String openid = "onbkn1YmGlP56xmTAsvEtzxLT3eE";
		System.out.println(WxService.getUserInfo(openid));
	}
    
    @Test
    public void testQrCode() {
		System.out.println(WxService.getQrCodeTicket());
	}
    
    
    @Test
    public void testUpload(){
//    	String file = "C://Users/10136/Desktop/1.png";
//    	String result = WxService.upload(file, "image");
//    	System.out.println(result);
    	//oQNfuVwZT2OlIhjYYdQOfPzuiBeOHwHDTPKAi4X4IcEnFyrmS_0Hf1X6xgi5AyrQ
    	//16_OxDrrNifvPkxYCxjstlj6i3b5gBiD7vCZgAyfJVI6wDQh6YRUfW9JLB6I6TWQ5-qf_JmIPv7ULAZwysVT3LtS1v_L6qy8y5vJ1_qJDzDy6eupiDsickTCBZGfDut11sjysa-vOkL6Rsj3nxpTCKbAFAIRZ
    	//https://api.weixin.qq.com/cgi-bin/media/get?access_token=16_OxDrrNifvPkxYCxjstlj6i3b5gBiD7vCZgAyfJVI6wDQh6YRUfW9JLB6I6TWQ5-qf_JmIPv7ULAZwysVT3LtS1v_L6qy8y5vJ1_qJDzDy6eupiDsickTCBZGfDut11sjysa-vOkL6Rsj3nxpTCKbAFAIRZ&media_id=oQNfuVwZT2OlIhjYYdQOfPzuiBeOHwHDTPKAi4X4IcEnFyrmS_0Hf1X6xgi5AyrQ
    }
    
    @Test
	public void testPic() {
		// 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "C://Users/10136/Desktop/1.png";
        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));

	}
	
	
	
	@Test
	public void testButton(){
		//菜单对象
		Button btn = new Button();
		//第一个一级菜单
		btn.getButton().add(new ClickButton("一级点击","1"));
		//第二个一级菜单
		btn.getButton().add(new ViewButton("一级跳转","https://www.baidu.com"));
		//创建弟三个一个菜单
		SubButton sb = new SubButton("有子菜单");
		//为第三个一级菜单增加子菜单
		sb.getSub_button().add(new PhotoOrAblumButton("传图", "31"));
		sb.getSub_button().add(new ClickButton("点击", "32"));
		sb.getSub_button().add(new ViewButton("网易新闻", "http://news.163.com"));
		btn.getButton().add(sb);
		//转为json
		JSONObject jsonObject = JSONObject.fromObject(btn);
		System.out.println(jsonObject.toString());
	}
	
	@Test
	 public void getToken() {
		System.out.println(WxService.getAccessToken());
		System.out.println(WxService.getAccessToken());
	}
	
	
	
	@Test
	public void testMsg(){
		Map<String, String> map = new HashMap<>();
		map.put("ToUserName", "to");
		map.put("FromUserName", "from");
		map.put("MsgType", "type");
		TextMessage tm = new TextMessage(map, "Hello");
		System.out.println(tm);
		
		XStream stream = new XStream();
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml = stream.toXML(tm);
		System.out.println(xml);
	}
}
