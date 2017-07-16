package cn.crap.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.sf.encryption.Encryption;

import net.sf.json.JSONObject;

public class HttpPostGetEgmas {
	// 处理EGMAS请求参数
	public static String egmasPost(String path, Map<String, String> params, Map<String, String> headers, String requestMethod) throws Exception {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000).setConnectionRequestTimeout(150000).setStaleConnectionCheckEnabled(true).build();
		// 请求的参数信息传递
		// List<NameValuePair> paires = new ArrayList<NameValuePair>();
		String paramStr = "";
		if (params != null) {
			StringBuilder sb = new StringBuilder();
			JSONObject jsonParams = JSONObject.fromObject(params);
			if (path.endsWith("/ucmp")) {
				path = path + "/" + requestMethod;
				sb.append("params=").append(jsonParams);
				// 无需加密
				paramStr = sb.toString();
			} else {
				JSONObject jsonRequest = new JSONObject();
				jsonRequest.put(Const.METHOD, requestMethod);
				jsonRequest.put(Const.REQUESTPARAMS, jsonParams);
				sb.append("params=").append(jsonRequest.toString());
				System.out.println("未加密前params = [" + sb.toString() + "]");
				paramStr = Encryption.encryCode(sb.toString()); // 加密，
				System.out.println("加密后paramStr = [" + paramStr + "]");
				paramStr = URLEncoder.encode(paramStr, "UTF-8");
				System.out.println("Encoder处理后加密后paramStr = [" + paramStr + "]");
			}
		}
		// if (paires.size() > 0) {
		HttpPost method = new HttpPost(path);
		BasicHttpEntity requestBody = new BasicHttpEntity();
		requestBody.setContent(new ByteArrayInputStream(paramStr.getBytes("UTF-8")));
		requestBody.setContentLength(paramStr.getBytes("UTF-8").length);
		method.setEntity(requestBody);
		// }
		method.setConfig(requestConfig);
		return postEgmasMethod(method, params, headers);
	}

	private static String postEgmasMethod(HttpUriRequest method, Map<String, String> params, Map<String, String> headers) throws Exception {
		HttpClient client = HttpClients.createDefault();
		method.setHeader("charset", "utf-8");

		// EGMAS默认头信息
		Map<String, String> defaultHeaders = new HashMap<String, String>();
		defaultHeaders.put("deviceId", "V2UrHNDU1sADAC/9JUNUBxV1");
		defaultHeaders.put("mediaCode", "ANDROID.APP");
		defaultHeaders.put("language_code", "zh_CN");
		defaultHeaders.put("langCode", "sc");
		defaultHeaders.put("platform", "Android");
		defaultHeaders.put("systemVersion", "6.0.1");
		defaultHeaders.put("systemCode", "ANDROID.APP");
		defaultHeaders.put("countryCode", "CN");
		defaultHeaders.put("Content-Type", "application/x-www-form-urlencoded");
		defaultHeaders.put("client_ver", "8.2.0");
		defaultHeaders.put("screen_size", "1440x2560");
		defaultHeaders.put("phoneId", "V2UrHNDU1sADAC/9JUNUBxV1");
		defaultHeaders.put("protocol_ver", "334");
		defaultHeaders.put("region_code", "CN");
		defaultHeaders.put("model", "Le X820");

		if (defaultHeaders != null) {
			Set<String> keys = defaultHeaders.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				method.setHeader(key, defaultHeaders.get(key));
			}
		}

		if (headers != null) {
			Set<String> keys = headers.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				method.setHeader(key, headers.get(key));
			}
		}
		HttpResponse response = client.execute(method);
		int status = response.getStatusLine().getStatusCode();
		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Path:" + method.getURI() + "-Unexpected response status: " + status);
		}
		HttpEntity entity = response.getEntity();
		String encoding = "UTF-8";
		try {
			Properties initProp = new Properties(System.getProperties());
			encoding = initProp.getProperty("file.encoding");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("GBK".equals(encoding)) {
			String body = EntityUtils.toString(entity, "UTF-8");
			body = Encryption.decryCode(body);
			String iso = new String(body.getBytes("GBK"), "ISO-8859-1");
			String tempBody2 = new String(iso.getBytes("ISO-8859-1"), "UTF-8");
			return tempBody2;
		} else {
			String oldBody = convertStreamToString(entity.getContent());
			String newBody = Encryption.decryCode(oldBody);
			return null == newBody || "".equals(newBody) ? oldBody : newBody;
		}
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
