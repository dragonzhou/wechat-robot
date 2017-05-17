package turing.test;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import turing.util.Aes;
import turing.util.Md5;
import turing.util.PostServer;

/**
 * 加密请求测试类
 * 
 * @author 图灵机器人
 *
 */
public class AesTest {

	public static void main(String[] args) {
		String cmd = "你叫什么";// 测试用例
		cmdAes(cmd);
	}

	@SuppressWarnings("unchecked")
	public static String cmdAes(String cmd) {
		// 图灵网站上的secret
		String secret = "f2624afa5120dd14";
		// 图灵网站上的apiKey
		String apiKey = "8ead3f3d6ff0bbadf695b5c80a5b20c8";
		// 待加密的json数据
		String data = "{\"key\":\"" + apiKey + "\",\"info\":\"" + cmd + "\"}";
		// 获取时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());

		// 生成密钥
		String keyParam = secret + timestamp + apiKey;
		String key = Md5.MD5(keyParam);

		// 加密
		Aes mc = new Aes(key);
		data = mc.encrypt(data);

		// 封装请求参数
		JSONObject json = new JSONObject();
		json.put("key", apiKey);
		json.put("timestamp", timestamp);
		json.put("data", data);
		// 请求图灵api
		String result = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
		System.out.println("图灵结果:" + result);
		String answer = "";
		if (null != result && !result.equals("")) {
			json = new JSONObject();
			Map<String, String> map = JSON.parseObject(result, Map.class);
			if (null != map && map.size() >= 0) {
				answer = map.get("text");
			}
		}
		return answer;

	}

}