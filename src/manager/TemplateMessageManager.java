package manager;

import org.junit.Test;

import Service.WxService;
import util.Util;

public class TemplateMessageManager {

	/**设置所属行业
	 * @return
	 */
	@Test
	public void setIndustry() {
		String at = WxService.getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token="+at;
		String data="{\r\n" + 
				"          \"industry_id1\":\"1\",\r\n" + 
				"          \"industry_id2\":\"4\"\r\n" + 
				"       }";
		String result = Util.post(url, data);
		System.out.println(result);
		
	}
	
	/**
	 * 获取所属行业
	 */
	@Test
	public void getIndustry() {
		String at = WxService.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token="+at;
		String result = Util.get(url);
		System.out.println(result);
	}
	
	@Test
	public void sendTemplateMessage() {
		String at = WxService.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+at;
		String data = "{\r\n" + 
				"	\"touser\": \"onbkn1fDU55ApZlXTa1qPLzd8708\",\r\n" + 
				"	\"template_id\": \"_QBy1OxRj0ueLr1yGuOa3wdONaVpb443mRl7gsgG7Wo\",\r\n" + 
				"	\"data\": {\r\n" + 
				"		\"first\": {\r\n" + 
				"			\"value\": \"恭喜你购买成功！\",\r\n" + 
				"			\"color\": \"#173177\"\r\n" + 
				"		},\r\n" + 
				"		\"time\": {\r\n" + 
				"			\"value\": \"巧克力\",\r\n" + 
				"			\"color\": \"#173177\"\r\n" + 
				"		},\r\n" + 
				"		\"address\": {\r\n" + 
				"			\"value\": \"39.8元\",\r\n" + 
				"			\"color\": \"#173177\"\r\n" + 
				"		},\r\n" + 
				"		\"remark\": {\r\n" + 
				"			\"value\": \"欢迎再次购买！\",\r\n" + 
				"			\"color\": \"#173177\"\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}";
		String result = Util.post(url, data);
		System.out.println(result);
	}
	
}
