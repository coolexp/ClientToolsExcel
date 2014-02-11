package com.coolexp.vo;

import java.io.File;

public class InputArgsVO {
	public String path;
	public boolean isTrue;
	public boolean isData;
	public boolean outAS;
	public boolean outJava;
	public String  outPrePath;
	public String defaultPackagePath;
	public boolean outCADD;
	public boolean isOutPutSheetsDat;
	public boolean isOutPutExcelHelper;
	public static InputArgsVO parse(String[]  args){
		InputArgsVO iao = new InputArgsVO();
		String path ="";
		boolean isTrue=false;
		String outPrePath="outputdata\\";
		String defaultPackagePath = "com.eray.data.vo";
		boolean isData = true;
		boolean outAS = false;
		boolean outJava = false;
		boolean outC = false;
		boolean outSheetsDat = false;
		boolean outPutExcelHelper = true;
		if (args.length < 6){
			System.out.println("参数不足,正在查找默认路径D:\\data");
			File file = new File("D:\\data");
			if(!file.exists()){
				System.out.println("默认路径 径不存在");
				System.exit(0);
			}
			path = "D:\\data";
			outPrePath = "D:\\outputdata\\";
			File fout = new File(outPrePath);
			if(!fout.exists()){
				fout.mkdir();
			}
		}else{
			path = args[0];
			isTrue = args[1].equals("true") ? true : false;
			isData = args[2].equals("1") ? true : false;
			outAS = args[3].equals("1") ? true : false;
			outJava = args[4].equals("1") ? true : false;
			outC = args[6].equals("1") ? true : false;
			outPutExcelHelper = args[7].equals("1") ? true : false;
			defaultPackagePath = args[5];
			if(args[7]!=null){
				outSheetsDat = args[7].equals("1") ? true : false;
			}
			
		}
		if(isData==false&&outAS==false&&outJava==false&&outC==false&&outPutExcelHelper==false){
			System.out.println("您输入的参数不需要做任何打包工作");
			System.exit(0);
		}
		iao.outCADD = outC;
		iao.path = path;
		iao.isTrue = isTrue;
		iao.isData = isData;
		iao.outAS = outAS;
		iao.outJava = outJava;
		iao.outPrePath = outPrePath;
		iao.defaultPackagePath = defaultPackagePath;
		iao.isOutPutSheetsDat = outSheetsDat;
		iao.isOutPutExcelHelper = outPutExcelHelper;
		return iao;
	}
}
