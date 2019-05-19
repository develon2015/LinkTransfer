package comm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;

public class SystemUtil {
	private static final String ConfigFileName = "config";
	private static final ResourceBundle resBun = ResourceBundle.getBundle(ConfigFileName);
	private static MessageDigest md5 = null;
	
	public static String getConfigParam(String key) {
		if (resBun != null) {
			return resBun.getString(key);
		}
		return null;
	}
	
	public static String hash(String url) {
		if (md5 == null) {
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		md5.reset();
		byte[] bs = md5.digest(url.getBytes());
		return DatatypeConverter.printHexBinary(bs).toUpperCase();
	}
	
	/**
	 * 对用户提交的 URL 进行处理
	 * @param url
	 * @return
	 */
	public static String handleURL(String url) {
		if (url == null) 
			return null;
		String result = url.trim();
		if (result.indexOf("http://") != 0 && result.indexOf("https://") != 0) {
			result = "http://" + url; // 对于未指定协议的 URL 添加 HTTP 协议
		}
		return result;
	}
}
