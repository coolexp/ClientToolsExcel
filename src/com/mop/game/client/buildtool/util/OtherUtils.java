package com.mop.game.client.buildtool.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherUtils {
	/**
	 * 显示信息
	 * 
	 * @param parent
	 *            父窗口
	 * 
	 * @param info
	 *            要显示的信息
	 */
//	public static void showInfo(String info) {
//		System.out.println(String.format("[信息]%s", info));
//	}

	public static void showError(String error) {
		System.out.println(String.format("[错误]%s", error));
	}

	/**
	 * 打印出消息
	 * 
	 * @param msg
	 *            要打印的消息
	 */
	public static void printMsg(String msg) {
		System.out.println(String.format("[%s] %s\n",
				OtherUtils.timestampToDate(), msg));
	}

	/**
	 * 将时间戳转换成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 格式化后的时间
	 */
	public static String timestampToDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(new Date());
		return dateString;
	}

	/**
	 * 判断某个字符串是否是数字
	 * 
	 * @param data
	 *            要判断字符串
	 * 
	 * @return true-是，false-否
	 */
	public static boolean isNumble(String data) {
		int length = data.length();
		char c;
		boolean back = true;
		for (int i = 0; i < length; i++) {
			c = data.charAt(i);
			if (c < '0' || c > '9') {
				back = false;
				break;
			}
		}
		return back;
	}

	/**
	 * 获取当前目录
	 * 
	 * @return
	 */
	public static String getCurrentPath() {
		String path;
		if (OtherUtils.class.getResource("/") != null) {
			path = OtherUtils.class.getResource("/").getPath();
			if (path.startsWith("/")
					&& System.getProperty("os.name").startsWith("Windows")) {
				path = path.substring(1);
			}
		} else {
			path = OtherUtils.class.getResource("").getPath();
			if (path.startsWith("file:")) {
				// 剪去前面的file:/
				int end = path.indexOf("jar!");
				path = path.substring(0, end);
				// 减去jar!后面的字符串
				end = path.lastIndexOf("/") + 1;
				path = path.substring("file:".length(), end);
			}
		}
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			OtherUtils.showError(e.toString());
		}

		return path;
	}

	/**
	 * 转换编码 ISO-8859-1到GB2312
	 * 
	 * @param text
	 * @return
	 */
	public static String ISO2GB(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			result = ex.toString();
		}
		return result;
	}
}
