package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.baidu.aip.ocr.AipOcr;
import com.thoughtworks.xstream.XStream;

import entity.AccessToken;
import entity.Article;
import entity.BaseMessge;
import entity.ImageMessage;
import entity.MusicMessage;
import entity.NewsMessage;
import entity.TextMessage;
import entity.VideoMessage;
import entity.VoiceMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.Util;




/**
* Title:WxService
* Description:
* Company: 
* @author Joey
* @date 2018年11月30日下午1:42:39
*/
public class WxService {
	private static String TOKEN="joey";
	private static String APPKEY="1fec136dbd19f44743803f89bd55ca62";
	//微信公众号
	private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String APPID="wx53ed51da0078d00b";
    private static final String APPSECRET="3f0aea26615e4f74549c356528e3c05f";
    //百度AI参数
    public static final String APP_ID = "15026081";
    public static final String API_KEY = "WrVkDdnWP2Ng2tvVhi25nrDp";
    public static final String SECRET_KEY = "WC1Vi7DNVrkUV5GYz3AOVYBoiyOyYR3n";
	
    //用于存储Token
    private static AccessToken at;
    /**
     * 获取Token
     */
    public static void getToken() {
    	String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
    	String tokenStr = Util.get(url);
    	JSONObject jsonObject = JSONObject.fromObject(tokenStr);
    	String token = jsonObject.getString("access_token");
    	String expireIn = jsonObject.getString("expires_in");
    	at = new AccessToken(token, expireIn);
	}
    
    /**向外暴露的过去Token的方法
     * @return
     */
    public static String getAccessToken(){
    	if(at==null||at.isExpired()){
    		getToken();
    	}
    	return at.getAccessToken();
    }
    
    
    
    
	/**验证签名
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp,String nonce,String signature) {
		//1）将token、timestamp、nonce三个参数进行字典序排序
		String[] strs = new String[]{TOKEN,timestamp,nonce};
		Arrays.sort(strs);
		//2）将三个参数字符串拼接成一个字符串进行sha1加密
		String str = strs[0]+strs[1]+strs[2];
		String mysig = sha1(str);
		//3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		return mysig.equals(signature); 	
		
	}
	/**进行sha1加密
	 * @param str
	 * @return
	 */
	private static String sha1(String str) {
		try {
			//获取一个加密对象
			MessageDigest md = MessageDigest.getInstance("sha1");
			//加密
			byte[] digest = md.digest(str.getBytes());
			char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuilder sb = new StringBuilder();
			//处理加密结果
			for(byte b:digest){
				sb.append(chars[(b>>4)&15]);
				sb.append(chars[b&15]);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Map<String, String> parseRequest(InputStream is) {
		Map<String,String> map	= new HashMap<>();
		SAXReader reader = new SAXReader();
		try {
			//读取输入流，获取文档对象
			Document document = reader.read(is);
			//根据文档对象获取根节点
			Element root = document.getRootElement();
			//获取根节点的所有子节点
			List<Element> elements = root.elements();
			for(Element element:elements){
				map.put(element.getName(), element.getStringValue());
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 用于处理所有的事件和消息的回复
	 * @param requestMap
	 * @return 返回XML数据包
	 */
	/**
	 * @param requestMap
	 * @return
	 */
	public static String getRespose(Map<String, String> requestMap) {
		BaseMessge msg = null ;
		String msgType = requestMap.get("MsgType");
		switch (msgType) {
		case "text":
			msg=dealTextMessage(requestMap);
			break;
		case "image":
			msg=dealImage(requestMap);
			break;
		case "voice":
			
			break;
		case "video":
			
			break;
		case "shortvideo":
			
			break;
		case "location":
			
			break;
			
		case "link":
			
			break;
		case "event":
			msg=dealEvent(requestMap);
			break;
		default:
			break;
		}
		//把消息对象处理为xml数据包
		return beanToXml(msg);
	}
	
	

	/**
	 * 把消息对象处理为xml数据包
	 * @param msg
	 * @return
	 */
	private static String beanToXml(BaseMessge msg) {
		XStream stream = new XStream();
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml = stream.toXML(msg);
		return xml;
	}
	
	/**处理图片识别
	 * @param requestMap
	 * @return
	 */
	private static BaseMessge dealImage(Map<String, String> requestMap) {
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
        String path = requestMap.get("PicUrl");
        
        //org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        org.json.JSONObject res = client.generalUrl(path, new HashMap<String, String>());
        System.out.println(res.toString(2));
        String json = res.toString();
        //转为JSONObject
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        Iterator<JSONObject> it = jsonArray.iterator();
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()){
        	JSONObject next = it.next();
        	sb.append(next.getString("words"));
        }
		return new TextMessage(requestMap, sb.toString());
	}
	
	/**处理事件推送
	 * @param requestMap
	 * @return
	 */
	private static BaseMessge dealEvent(Map<String, String> requestMap) {
		String event = requestMap.get("Event");
		System.out.println(event);
		System.out.println("Click".equals(event));
		if("CLICK".equals(event)){
			return dealClick(requestMap);
		}
		else if("VIEW".equals(event)){
			return dealView(requestMap);
		}
		return null;
	}
	
	/** 处理View类型的按钮
	 * @param requestMap
	 * @return
	 */
	private static BaseMessge dealView(Map<String, String> requestMap) {
		
		return null;
	}

	/** 处理Click类型的按钮
	 * @param requestMap
	 * @return
	 */
	private static BaseMessge dealClick(Map<String, String> requestMap) {
		String key = requestMap.get("EventKey");
		//菜单对应的Key
		switch (key) {
		//点击一级菜单
		case "1":
			//处理点击第一个一级菜单
			System.out.println("你点了第一个一级菜单");
			return new TextMessage(requestMap,"你点了第一个一级菜单");
		case "32":
			//处理点击第一个一级菜单
			System.out.println("你点了第三个一级菜单的子菜单");
			return new TextMessage(requestMap,"你点了第三个一级菜单的子菜单");
		default:
			break;
		}
		return null;
	}

	/**
	 * 处理文本消息
	 * @param requestMap
	 * @return
	 */
	private static BaseMessge dealTextMessage(Map<String, String> requestMap) {
		//用户发送的消息
		String msg = requestMap.get("Content");
		if(msg.equals("图文")){
			List<Article> articles = new ArrayList<>();
			articles.add(new Article("标题哦", "详细介绍", "http://mmbiz.qpic.cn/mmbiz_jpg/iaTXlj4hknDJwzYUITHibAMmibYhkciaf8XLe8fLnXy8wAZj4h2sakibn4jh8GcLZiadjRrBlPMaGtjTYibSKFiaP3L4Sw/0", "https://www.baidu.com"));
			NewsMessage nm = new NewsMessage(requestMap, articles);
			return nm;
		}
		if(msg.equals("登录")){
			String redirect_uri = "http://wxin.free.idcfengye.com/weixin/GetUserInfo";
			String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx53ed51da0078d00b&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo#wechat_redirect";
			TextMessage tm = new TextMessage(requestMap, "点击<a href=\""+url+"\">这里</a>登录");
			return tm;
		}
		//调用方法返回聊天的内容
		String resp = chat(msg);
		TextMessage tm = new TextMessage(requestMap, resp);
		return tm;
	}
	
	/**
	 * 调用机器人聊天
	 * @param msg
	 * @return
	 */
	private static String chat(String msg) {
		
        String result =null;
        String url ="http://op.juhe.cn/robot/index";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请到的本接口专用的APPKEY
        params.put("info",msg);//要发送给机器人的内容，不要超过30个字符
        params.put("dtype","");//返回的数据的格式，json或xml，默认为json
        params.put("loc","");//地点，如北京中关村
        params.put("lon","");//经度，东经116.234632（小数点后保留6位），需要写为116234632
        params.put("lat","");//纬度，北纬40.234632（小数点后保留6位），需要写为40234632
        params.put("userid","");//1~32位，此userid针对您自己的每一个用户，用于上下文的关联
        try {
            result =Util.net(url, params, "GET");
            //解析Json
            JSONObject jSONObject =  JSONObject.fromObject(result);
            //取出error_code
            int code = jSONObject.getInt("error_code");
            if(code!=0){
            	return null;
            }
            String resp = jSONObject.getJSONObject("result").getString("text");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }  
	return null;
	}
	
	/**
	 * 上传临时素材
	 * @param path
	 * @param type
	 * @return
	 */
	public static String upload(String path,String type){
		File file = new File(path);
		//地址
		String url="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		url=url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
		try {
			URL urlObj = new URL(url);
			//强转为安全连接
			HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
			//设置链接的信息
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			//设置请求头信息
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "utf8");
			//数据边界
			String boundary = "-----"+System.currentTimeMillis();
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			//获取输出流
			OutputStream out = connection.getOutputStream();
			//创建文件的输入流
			InputStream is = new FileInputStream(file);
			//第一部分：头部信息
			//准备头部信息
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n");
			sb.append("Content-Type:application-stream\r\n\r\n");
			out.write(sb.toString().getBytes());
			//第二部分：文件内容
			byte[] b = new byte[1024];
			int len;
			while((len=is.read(b))!=-1){
				out.write(b, 0, len);
			}
			//第三部分：尾部信息
			String foot = "\r\n--" + boundary +"--r\n";
			out.write(foot.getBytes());
			out.flush();
			out.close();
			//读取数据
			InputStream inputStream = connection.getInputStream();
			StringBuilder resp = new StringBuilder();
			while((len=inputStream.read(b))!=-1){
				resp.append(new String(b,0,len));
			}
			return resp.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 获取带参数二维码的ticket
	 * @return
	 */
	public static String getQrCodeTicket() {
		String at = getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+at;
		//生成临时字符二维码
		String data="{\r\n" + 
				"	\"expire_seconds\": 600,\r\n" + 
				"	\"action_name\": \"QR_STR_SCENE\",\r\n" + 
				"	\"action_info\": {\r\n" + 
				"		\"scene\": {\r\n" + 
				"			\"scene_str\": \"test\"\r\n" + 
				"		}\r\n" + 
				"	}\r\n" + 
				"}";
		String result = Util.post(url, data);
		String ticket = JSONObject.fromObject(result).getString("ticket");
		
		return ticket;
	}
	
	/**
	 * 获取用户信息
	 * @param openid
	 * @return
	 */
	public static String getUserInfo(String openid) {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openid);
		String result = Util.get(url);
		return result;
		
	}
}
