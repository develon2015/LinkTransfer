package comm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadProgress implements Runnable {
	Thread downloadThread;
	URL url;
	File file;
	Statu statu = Statu.DOWNLOADING;
	long count = 0;
	long content_length = 0;
	
	/**
	 * 下载状态
	 * @author Administrator
	 *
	 */
	public static enum Statu {
		DOWNLOADING,
		SUCCEED,
		FAILED,
	}
	
	static {
		System.setProperty("http.agent", "Chrome");
		HttpURLConnection.setFollowRedirects(true);
		HttpsURLConnection.setFollowRedirects(true);
	}
	
	public DownloadProgress(URL url, File file) {
		// 一个 下载进度 对象产生的时候, 下载就开始了
		try {
			this.file = file;
			System.out.println("存储为 :  " + file.getAbsolutePath());
			this.url = url;
			System.out.println("当前下载对象: " + url);
			HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn = handleRedirect(conn, "HEAD");
			long content_length = conn.getContentLengthLong();
			if (conn.getResponseCode() != 200 || content_length <= 0) {
				conn.disconnect();
				// 有的服务器不支持 HEAD 协议, 尝试 GET 协议但不读流
				HttpURLConnection conn2 = (HttpURLConnection) this.url.openConnection();
				conn2 = handleRedirect(conn2, "GET");
				content_length = conn2.getContentLengthLong();
				if (conn2.getResponseCode() != 200 || content_length <= 0) {
					conn.disconnect();
					throw new RuntimeException(String.format("不支持的类型(%d, %d)", conn.getResponseCode(), content_length));
				}
			}
			this.content_length = content_length;
			downloadThread = new Thread(this, file.getAbsolutePath());
			conn.disconnect();
			file.createNewFile();
			downloadThread.start();
		} catch (Throwable e) {
			throw new RuntimeException("无法启动下载: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		statu = Statu.DOWNLOADING;
		System.out.println("开始下载 " + url.toString());
		FileOutputStream fos = null;
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) this.url.openConnection();
			conn.setRequestMethod("GET");
			conn = handleRedirect(conn, "GET");
			is = conn.getInputStream();
			fos = new FileOutputStream(file);
			byte[] bs = new byte[1024 * 1024];
			int n = 0;
			while ((n = is.read(bs)) != -1) {
				fos.write(bs, 0, n);
				count += n;
			}
			System.out.printf("下载 %s 成功(%d/%d) \n", url.toString(), count, content_length);
			statu = Statu.SUCCEED;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Thread.currentThread().getName() + " 下载异常: " + e.toString());
			this.statu = Statu.FAILED;
		} finally {
			try {
				fos.close();
			} catch (IOException e) { }
			try {
				is.close();
			} catch (IOException e) { }
			conn.disconnect();
		}
	}
	
	/**
	 * 打开连接并处理重定向
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public HttpURLConnection handleRedirect(HttpURLConnection conn, String method) throws Exception {
		conn.setRequestMethod(method);
		conn.connect();
		String sheet = conn.getHeaderField("Location");
		if (sheet == null)
			return conn;
		URL newURL = null;
		System.out.println("一个重定向: " + sheet);
		try {
			newURL = new URL(sheet);
			HttpURLConnection newConn = (HttpURLConnection) newURL.openConnection();
			newConn.setRequestMethod(method);
			newConn = handleRedirect(newConn, method);
			return newConn;
		} catch (Exception e) {
			// sheet 不是一个完整的 URL, 
			throw new RuntimeException("不支持的重定向: " + sheet);
		}
	}
	
	public Statu getStatu() {
		return statu;
	}
	
	@Override
	public String toString() {
		switch (statu) {
		case DOWNLOADING:
			break;
		case FAILED:
			throw new RuntimeException("下载已然失败");
		default:
		}
		return String.format("%.2f %%", 100.0*count/content_length);
	}
}
