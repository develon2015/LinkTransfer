package comm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import comm.DownloadProgress.Statu;

public class Transfer {
	/** 每个 URL 对应一个 下载进度 对象, 使用哈希表存储 */
	private final HashMap<String, DownloadProgress> list = new HashMap<String, DownloadProgress>();
	private final HashMap<String, File> listFile = new HashMap<String, File>();
	private static final Transfer thiz = new Transfer();

	public Transfer() {
	}

	/**
	 * 提交下载请求, 查询下载进度
	 * 
	 * @param url
	 * @return 是否正在下载
	 * @throws Runtime 如果下载失败
	 */
	public Statu doDownload(String url) {
		url = SystemUtil.handleURL(url);
		URL targetURL = null;
		try {
			targetURL = new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.toString());
		}
		DownloadProgress dp = list.get(url);
		if (dp == null) {
			// 初次提交下载
			System.out.print("下载: " + url);
			System.out.println(" 哈希值: " + SystemUtil.hash(url));
			String md5 = SystemUtil.hash(url);
			File tmpFile = new File(SystemUtil.getConfigParam("static_root_directory") + File.separator + targetURL.getPath().replaceAll(".*/", md5 + " - ") );
			dp = new DownloadProgress(targetURL, tmpFile);
			list.put(url, dp);
			listFile.put(md5, tmpFile);
		}
		return dp.getStatu();
	}

	/**
	 * 查询下载进度
	 * 
	 * @return
	 */
	public String queryProgress(String url) {
		url = SystemUtil.handleURL(url);
		DownloadProgress dp = list.get(url);
		if (dp == null)
			return "请稍后再试";
		return dp.toString();
	}
	
	public String queryDownLink(String md5) {
		File file = listFile.get(md5);
		if (file == null)
			return "err";
		return SystemUtil.getConfigParam("transfer") + "/" + file.getName();
	}

	public static Transfer getInst() {
		return thiz;
	}

	public HashMap<String, DownloadProgress> getList() {
		return list;
	}
}
