package com.mop.game.client.buildtool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 标题: 文件操作工具.<br>
 * 
 * 描述：文件操作工具，主要有：读取模板.<br>
 * 
 * 版权：Copyright (C) 1997-2008 Oak Pacific Interactive. <br>
 * 
 * @author 黄杰 2010-06-011 10:50 新建
 * 
 * @since jdk1.6
 * 
 * @version 0.1
 */
public class FileUtil {

	/**
	 * 读取模板
	 * 
	 * @param path
	 *            模板路径
	 * @return 返回模板内容
	 * @throws IOException
	 *             向上抛出读取错误
	 */
	public static String readTemplate(String path) throws IOException {
		StringBuilder sb = new StringBuilder();
		File f = new File(path);
		FileInputStream fin = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		String tmp;
		while ((tmp = br.readLine()) != null) {
			sb.append(tmp);
			sb.append("\n");
		}
		fin.close();
		return sb.toString();
	}

	/**
	 * 写文件
	 * 
	 * @param path
	 *            要写文件路径
	 * @param content
	 *            要写的文件内容
	 * @throws IOException
	 *             向上抛出写文件错误
	 */
	public static void writeTemplate(String path, String content)
			throws IOException {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(content.getBytes("UTF-8"));
		fos.close();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 *            要创建的路径
	 */
	public static void builderPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
