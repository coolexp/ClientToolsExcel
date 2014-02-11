package com.mop.game.client.buildtool.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.Set;
import java.util.zip.DeflaterOutputStream;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.coolexp.vo.ErayExcelManagerBean;
import com.coolexp.vo.InputArgsVO;
import com.coolexp.vo.ObjKeyVO;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

/**
 * 标题: Excel读取工具.<br>
 * 
 * 描述：Excel读取工具，主要用于读取excel中的数据.<br>
 * 
 * 版权：Copyright (C) 1997-2008 Oak Pacific Interactive. <br>
 * 
 * @author 黄杰 2010-09-08 10:50 新建
 * 
 * @since jdk1.6
 * 
 * @version 0.1
 */
public class TranformExcel {

	Element root;
	Document doc;
	String outputfile = "ErayClientData.xml";
	String outputDatafile = "ErayClientData.dat";
	String outputDatafileAmf = "ErayClientData.amf";
	String outputXMLFile = "ErayClassObj.xml";
	String keySheetsName = "SheetsName";
	String[] rootNames = { "root", "data" };
	String[] classNodNames = { "root", "cl" };
	Set<String> whiteSet = new HashSet<String>();
	Set<String> whiteSetOld = new HashSet<String>();
	Boolean outPutData = true;
	Boolean outPutAS = false;
	Boolean outPutJava = false;
	Boolean outPutCDD = false;
	String basePath="";
	String defaultPackageString;
	Element classRoot;
	Document classDoc;
	ArrayList<String> allClassList = new ArrayList<String>();
	Map<String, Object> mainMap = new HashMap<String, Object>();
	ArrayList<ErayExcelManagerBean> fieldFacList = new ArrayList<ErayExcelManagerBean>();
	private InputArgsVO iao;
	private boolean outSheestDat;
	public void setInputArgsVO(InputArgsVO _iao){
		iao = _iao;
	}
	/**
	 * 转换一个根路径下所有的xls文件
	 * 
	 * @param path
	 *            根路径
	 * @throws Exception 
	 */
	public void tranformPath(String path, boolean line,Boolean isData,Boolean isAS, Boolean isJava,Boolean isCADD) throws Exception {
		outPutData = isData;
		outPutAS = isAS;
		outPutJava = isJava;
		outPutCDD = isCADD;
		root = new Element(rootNames[0]);
		doc = new Document(root);

		classRoot = new Element(classNodNames[0]);
		classDoc = new Document(classRoot);
		
		List<String> list = getAllFiles(path);
		
		for (String s : list) {
			if (FilenameUtils.isExtension(s, "xls")) {
				System.out.println("file:" + s);
				this.tranformExcel(s);
			}
		}

		FreeMarkerHelper.createVOFactory(basePath, allClassList, outPutCDD);
		/**输出数据XML节点文件**/
		XMLOutputter xmlout = new XMLOutputter();
		try {
			xmlout.output(doc, new FileOutputStream(outputfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**输出ClassXML节点文件**/
		XMLOutputter classXMLout = new XMLOutputter();
		try{
			classXMLout.output(classDoc, new FileOutputStream(outputXMLFile));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		createData(outputDatafile,mainMap);
	}
	public void setOutputFilePath(String path,String defaultPackageStr,boolean isOutPutSheesDat){
		outputfile = path +outputfile;
		outputDatafile = path +outputDatafile;
		outputDatafileAmf = path +outputDatafileAmf;
		outputXMLFile = path+outputXMLFile;
		basePath = path;
		defaultPackageString = defaultPackageStr;
		outSheestDat = isOutPutSheesDat;
	}
	private static void createData(String path,Map<String, Object> subMap) throws IOException{
		
		SerializationContext serializationContext = new SerializationContext();
		serializationContext.legacyCollection = true;
		Amf3Output amfOut = new Amf3Output(serializationContext);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		amfOut.setOutputStream(dataOutputStream);
		amfOut.writeObject(subMap);
		dataOutputStream.flush();
		byte[] messageBytes = outputStream.toByteArray();
		try {
			DeflaterOutputStream outputStream2 = new DeflaterOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
			outputStream2.write(messageBytes);
			outputStream2.flush();
			outputStream2.close();
			amfOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static List<String> getAllFiles(String absDir) {
		List<String> files = new ArrayList<String>();

		File fileDir = new File(absDir);
		String[] list = fileDir.list();
		for (String s : list) {
			String name = absDir + "/" + s;
			File ins = new File(name);
			if (ins.isFile()) {
				files.add(name);
			} else {
				files.addAll(getAllFiles(name));
			}
		}

		return files;
	}

	/**
	 * 转换一个xls文件
	 * 
	 * @param configExcelPath
	 *            要转换的xls的路径
	 */
	private void tranformExcel(String configExcelPath) throws Exception{
		String[] sheetNames = null;
		try {
			// 获取sheet的名字
			// long s = System.currentTimeMillis();
			sheetNames = ExcelUtil.getSheetName(configExcelPath);
			// System.out.println("sheetNames:" +(System.currentTimeMillis() -
			// s));
		} catch (Exception e) {
			OtherUtils.showError("获取sheet的名字失败");
			e.printStackTrace();
			System.exit(0);
		}
		// 转换所有sheet的数据
		String sheetName = null;
		Map<String,ObjKeyVO> objKeyMap = null;
		objKeyMap = ExcelUtil.getKeysList(configExcelPath,this.keySheetsName);
		try {
			for (int i = 0; i < sheetNames.length; i++) {
				sheetName = sheetNames[i];
				if(sheetName.equals(keySheetsName)){
					continue;
				}
				this.tranformSheet2(configExcelPath, sheetName,objKeyMap);
			}
			if(iao.isOutPutExcelHelper){
				FreeMarkerHelper.createExcelManagerStringFactory(iao.outPrePath, fieldFacList);
				FreeMarkerHelper.createExcelManagerFactory(iao.outPrePath, fieldFacList);
			}
		} catch (Exception e) {
			OtherUtils.showError(String.format("读取配置文件失败，请检查!\n路径：%s\n页名:%s",configExcelPath, sheetName));
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static short DEFAULT = 0;

	
	/**
	 * 将excel文件一个sheet转换成as
	 * 
	 * @param path
	 *            xls的路径
	 * @param sheetName
	 *            sheet的名字
	 */
	@SuppressWarnings("rawtypes")
	private void tranformSheet2(String path, String sheetName,Map<String,ObjKeyVO> objKeyMap) throws Exception {
		if ("changelog".equalsIgnoreCase(sheetName)) {
			return;
		}
		// 获取原始数据
		String[][] data = ExcelUtil.getSheetData(path, sheetName);
		ObjKeyVO ovo = objKeyMap.get(sheetName);
		
		if (data.length == 0) {
			return;
		}
		String xlsName = FilenameUtils.getBaseName(path);
		String className = changeChineseName(xlsName) + "_"
				+ changeChineseName(sheetName);

		// 首字母大写
		className = className.substring(0, 1).toUpperCase()
				+ className.substring(1);
		if (!whiteSetOld.contains(className)) {
			whiteSet.add(className);
		}
		Element subroot = new Element(className);
		Map<String, Object> subMap= new HashMap<String, Object>();
		// 获取是否辅助列
		String[] assistColumn = data[3];
		boolean isAssist = false;
		for (int j = 1; j < assistColumn.length; j++) {
			if (assistColumn[j].equals("1") || assistColumn[j].equals("3")) {
				isAssist = true;
				break;
			}
		}
		if (!isAssist) {
			return;
		}
		String tmp;
		Class iv = null;

		iv = null;
		if(iao.isOutPutExcelHelper){
			ErayExcelManagerBean managerBean = new ErayExcelManagerBean();
			String managerClassName = xlsName+"_"+sheetName+"Manager";
			managerBean.claString = managerClassName;
			managerBean.staticString = (xlsName+"_"+sheetName).toLowerCase();
			managerBean.uuString = (xlsName+"_"+sheetName).toUpperCase();
			fieldFacList.add(managerBean);
			FreeMarkerHelper.createExcelHelper(managerClassName,iao.isOutPutExcelHelper, data, iao.outPrePath,ovo);
		}
		//生成类文件
		FreeMarkerHelper.createClassVO(className, outPutAS, outPutJava, data,basePath,defaultPackageString,outPutCDD);
		Element classSubroot = new Element(this.classNodNames[1]);
		classSubroot.setAttribute("val",defaultPackageString+"."+className);
		classRoot.addContent(classSubroot);
		if(!outPutData){
			return;
		}
		allClassList.add(className);
		for (int i = 5; i < data.length; i++) {
			Map<String, String> ssubMap = new HashMap<String, String>();
			Element ssubroot = new Element(rootNames[1]);
			ssubroot.setAttribute("id", data[i][0]);
			//ssubMap.put("id", data[i][0]);
			Object oInstance = null;
			if(iv!=null){
				oInstance = iv.newInstance();
			}
			attributeVal("id",data[i][0],oInstance,ssubMap,iv,className);
			if (!whiteSetOld.contains("id")) {
				whiteSet.add("id");
			}
			for (int j = 1; j < assistColumn.length; j++) {
				// 数据有效
				if (assistColumn[j].equals("1") || assistColumn[j].equals("3")) {
					if (!whiteSetOld.contains(data[0][j].trim())) {
						whiteSet.add(data[0][j].trim());
					}
					// 需要国际化 生成key
					if (data[4][j].equals("1")) {
						ssubroot.setAttribute(data[0][j].trim(), getKey(path,sheetName, data[i][0], data[0][j]));
						//ssubMap.put(data[0][j].trim(), getKey(path,sheetName, data[i][0], data[0][j].trim()));
						attributeVal(data[0][j].trim(),getKey(path,sheetName, data[i][0], data[0][j].trim()),oInstance,ssubMap,iv,className);
					} else {
						// 数据类型
						tmp = (data[1][j]).toLowerCase();
						if (tmp.indexOf("string") != -1) {
							ssubroot.setAttribute(data[0][j].trim(), data[i][j]);
							//ssubMap.put(data[0][j].trim(), data[i][j]);
							attributeVal(data[0][j].trim(),data[i][j],oInstance,ssubMap,iv,className);
						} else {
							if (data[i][j] == null || "".equals(data[i][j].trim())) {
								ssubroot.setAttribute(data[0][j].trim(), String.valueOf(DEFAULT));
//								ssubMap.put(data[0][j].trim(), String.valueOf(DEFAULT));
								attributeVal(data[0][j].trim(),String.valueOf(DEFAULT),oInstance,ssubMap,iv,className);
								continue;
							} else {
								ssubroot.setAttribute(data[0][j].trim(), data[i][j]);
//								ssubMap.put(data[0][j].trim(), data[i][j]);
								attributeVal(data[0][j].trim(),data[i][j],oInstance,ssubMap,iv,className);
							}
						}
					}
				}
			}
			String objKey = getObjKey(ovo,data,i);
			subroot.addContent(ssubroot);
			if(iv!=null&&oInstance!=null){
				subMap.put(objKey, oInstance);
			}else{
				subMap.put(objKey, ssubMap);
			}
//			subMap.put(data[i][0], ssubMap);
		}
		
		Map<String,List<Object>> fieldMap = getGroupToList(subMap,ovo);
		mainMap.put(className+"_field", fieldMap);
		root.addContent(subroot);
		mainMap.put(className, subMap);
		if(outSheestDat){
			Map<String, Object> mMap = new HashMap<String, Object>();
			mMap.put(className, subMap);
			createData(basePath+className+".dat",mMap);
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String,List<Object>> getGroupToList(Map<String, Object> subMap,ObjKeyVO ovo) throws Exception{
		Map<String,List<Object>> map = new HashMap<String,List<Object>>();
		if(ovo==null){
			return map;
		}
		String searchStr = ovo.searchAttr;
		if(!searchStr.equals("")){
			String [] stringArr= searchStr.split(",");
			for(int i=0;i<stringArr.length;i++){
				String fieldName = stringArr[i];
				List<Object> objList = null;
				for (Object obj:subMap.values()){
					String value = "";
					if(obj instanceof Map){
						Map<String, String> map2 = (Map<String,String>) obj;
						Map<String,String> m = map2;
						if(fieldName.equals("ID")||fieldName.equals("id")){
							String idKey = fieldName.toLowerCase();
							value = m.get(idKey);
						}else{
							value = m.get(fieldName);
						}
					}else{
						Class iv = obj.getClass();
						Field fid = iv.getDeclaredField(fieldName);
						value = fid.get(obj).toString();
					}
					String objKeyStr = fieldName+"_"+value;
					objList = map.get(objKeyStr);
					if(objList==null){
						objList = new ArrayList<Object>();
						map.put(objKeyStr, objList);
					}
					objList.add(obj);
				}
			}
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private void attributeVal(String attrName,String val,Object o,Map<String, String> ssubMap,Class iv,String sheetName) throws Exception{
		if(o!=null){
			Field f = iv.getField(attrName);
			f.setAccessible(true);
			String typeStr = f.getType().toString();
			if(typeStr.equals("int")){
				int v = 0;
				if(!val.equals("")){
					try{
						v = Integer.parseInt(val);
					}catch(Exception e){
						System.out.println("页名："+sheetName+" "+attrName+val+"Int 出现异常");
						v = 0;
					}
				}
				f.set(o, v);
			}else if(typeStr.equals("long")){
				long t = 0;
				if(!val.equals("")){
					try{
						t = Long.parseLong(val);
					}catch(Exception e){
						System.out.println("页名："+sheetName+" "+attrName+val+"Long出现异常");
						t = 0;
					}
				}
				f.set(o, t);
			}else{
				f.set(o, val);
			}
		}else{
			ssubMap.put(attrName, val);
		}
	}
	
	private String getObjKey(ObjKeyVO ovo,String[][] data ,int i){
		String val="";
		String objKey = null;
		if(ovo!=null){
			val = ovo.keyStr;
		}
		if(val==null||val.equals("")){
			objKey = data[i][0];
		}else{
			String [] stringArr= val.split(",");
			objKey = "";
			StringBuilder sb = new StringBuilder(); 
			for(int ik = 0;ik<stringArr.length;ik++){
				int index = getIndex(data,stringArr[ik]);
				if(index==-1){
					sb = new StringBuilder();
					sb.append(data[i][0]);
					break;
				}
				sb.append(data[i][index]+"_");
			}
			objKey = sb.toString();
			objKey = objKey.substring(0, objKey.length()-1);
		}
		return objKey;
	}
	
	private int getIndex(String[][] data,String val){
		for(int i =0;i<data[0].length;i++){
			String  d = data[0][i];
			if(d.equals(val)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * 得到key excel名_sheet的index（从0开始）_每行的id_列的index（从0开始）
	 * 
	 * @param path
	 * @param sheetName
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
	private String getKey(String path, String sheetName, String id,
			String columnName) throws Exception {
		String xlsName = FilenameUtils.getBaseName(path);
		HSSFWorkbook workBook = ExcelUtil.getWorkBook(path);
		// 得到key
		return changeChineseName(xlsName) + "_"
				+ ExcelUtil.getSheetIndex(workBook, sheetName) + "_" + id + "_"
				+ ExcelUtil.getColumnIndex(workBook, sheetName, columnName);
	}


	public static String changeChineseName(String str) {
		// StringBuilder sb = new StringBuilder();
		StringBuffer sb = new StringBuffer();
		char[] arr = str.toCharArray();
		HanyuPinyinOutputFormat outFormat = new HanyuPinyinOutputFormat();
		outFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		outFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		// outFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							arr[i], outFormat);
					// for (String s : temp) {
					// sb.append(s);
					// }
					sb.append(temp[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				sb.append(arr[i]);
			}
		}
		return sb.toString();
	}
}
