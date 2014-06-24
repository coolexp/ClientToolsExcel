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
	public String outXMLPath;
	public String outDatPath;
	public boolean isOutSheetsXML;
	public static InputArgsVO parse(String[]  args){
		InputArgsVO iao = new InputArgsVO();
		String path ="";
		boolean isTrue=false;
		String outPrePath="outputdata\\";
		String outXMLPath="xml\\";
		String outTotalDatPath = "dat\\";
		String defaultPackagePath = "com.eray.data.vo";
		boolean isData = true;
		boolean outAS = false;
		boolean outJava = false;
		boolean outC = false;
		boolean outSheetsDat = false;
		boolean outPutExcelHelper = false;
		boolean outSheetsXML = false;
		boolean usePreBasePath = false;
		String preBasePath ="";
		if (args.length != 14){
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
			outXMLPath = "D:\\outputdata\\xml";
			File xmlOut = new File(outXMLPath);
			if(!xmlOut.exists()){
				xmlOut.mkdir();
			}
			outTotalDatPath = "D:\\outputdata\\dat";
			File datOut = new File(outTotalDatPath);
			if(!datOut.exists()){
				datOut.mkdir();
			}
		}else{
			path = args[0];
			isTrue = args[1].equals("1") ? true : false;
			isData = args[2].equals("1") ? true : false;
			outAS = args[3].equals("1") ? true : false;
			outJava = args[4].equals("1") ? true : false;
			defaultPackagePath = args[5];
			outC = args[6].equals("1") ? true : false;
			outPutExcelHelper = args[7].equals("1") ? true : false;
			outSheetsDat = args[8].equals("1") ? true : false;
			outSheetsXML = args[9].equals("1") ? true : false;
			usePreBasePath = args[10].equals("1") ? true : false;
			preBasePath = args[11];
			outXMLPath = args[12];
			outTotalDatPath = args[13];
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
		iao.outXMLPath = outXMLPath;
		iao.outDatPath = outTotalDatPath;
		iao.isOutSheetsXML = outSheetsXML;
		if(usePreBasePath){
			iao.path = preBasePath+path;
			iao.outDatPath = preBasePath+outTotalDatPath;
			iao.outXMLPath = preBasePath+outXMLPath;
		}
		return iao;
	}
}
