package weixin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import util.Util;

@WebServlet("/GetUser")
public class GetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetUserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取code
		String code = request.getParameter("code");
		//换取access_token的地址
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		url = url.replace("APPID", "wx53ed51da0078d00b").replace("SECRET", "3f0aea26615e4f74549c356528e3c05f");
		String result = Util.get(url);
		String at = JSONObject.fromObject(result).getString("access_token");
		String openid = JSONObject.fromObject(result).getString("openid");
		//拉取用户的基本信息
		url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url = url.replace("ACCESS_TOKEN", at).replace("OPENID", openid);
		result = Util.get(url);
		System.out.println(result);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
