package com.mop.game.client.buildtool.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


import com.coolexp.vo.ErayBean;
import com.coolexp.vo.ErayClassNameBean;
import com.coolexp.vo.ErayExcelManagerBean;
import com.coolexp.vo.ErayFunBean;
import com.coolexp.vo.ErayFunctionNameBean;
import com.coolexp.vo.ObjKeyVO;
import com.mop.game.client.buildtool.config.BuildConfig;
import com.mop.game.client.buildtool.config.FreemarkerConfiguration;


import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerHelper {
	public static void createVOFactory(String outPutBasePath,ArrayList<String> sheetNames,Boolean isCADD) throws Exception{
		if(isCADD){
			ArrayList<ErayClassNameBean> fieldList = new ArrayList<ErayClassNameBean>();
			for(int k=0;k<sheetNames.size();k++){
				String sheetName = sheetNames.get(k);
				ErayClassNameBean b = new ErayClassNameBean();
				String className = sheetName;
				b.classNameStr = className.trim();
				fieldList.add(b);
			}
			createVOFactoryFile(outPutBasePath,fieldList);
		}
	}
	private static void createVOFactoryFile(String basePath,ArrayList<ErayClassNameBean> fieldList)throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			try{
				template = freemarkerCfg.getTemplate(cc.getFacTemplatePath(),Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					File asDic = new File(basePath+"factory\\");
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+"factory\\"+"VOFactory.cpp"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public static void createExcelManagerFactory(String outPutBasePath,ArrayList<ErayExcelManagerBean> fieldFacList) throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			template = freemarkerCfg.getTemplate(cc.getExcelSheetHelperPath(),Locale.ENGLISH);
			template.setEncoding("UTF-8");
			HashMap<Object, Object> root = new HashMap<Object, Object>();
			root.put("FieldList", fieldFacList);
			Writer writer;
			try{
				String packageFolder = "helper\\";
				File asDic = new File(outPutBasePath+"as3\\"+packageFolder);
				if(!asDic.exists()){
					asDic.mkdirs();
				}
				writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outPutBasePath+"as3\\"+packageFolder+"ErayExcelDataHelper.as"), "UTF-8"));
				template.process(root, writer);
				writer.flush();
				writer.close();
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public static void createExcelManagerStringFactory(String outPutBasePath,ArrayList<ErayExcelManagerBean> fieldFacList) throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			template = freemarkerCfg.getTemplate(cc.getExcelStringHelperPath(),Locale.ENGLISH);
			template.setEncoding("UTF-8");
			HashMap<Object, Object> root = new HashMap<Object, Object>();
			root.put("FieldList", fieldFacList);
			Writer writer;
			try{
				String packageFolder = "helper\\";
				File asDic = new File(outPutBasePath+"as3\\"+packageFolder);
				if(!asDic.exists()){
					asDic.mkdirs();
				}
				writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outPutBasePath+"as3\\"+packageFolder+"DotaSheetsNameData.as"), "UTF-8"));
				template.process(root, writer);
				writer.flush();
				writer.close();
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	
	public static void createExcelHelper(String className,Boolean isOut,String[][] data,String outPutBasePath,ObjKeyVO ovo) throws IOException{
		if(!isOut){
			return;
		}
		String methodStr = ovo.methodStr;
		if(methodStr==null){
			System.out.println(className+ "表名不存在methodStr");
		}
		String [] stringArr= methodStr.split(",");
		String functionName = "";
		ArrayList<ErayFunctionNameBean> fieldList = new ArrayList<ErayFunctionNameBean>();
		for(int i=0;i<stringArr.length;i++){
			ErayFunctionNameBean beanFuncName = new ErayFunctionNameBean();
			String funcStr = stringArr[i];
			String [] funcList = funcStr.split("&");
			functionName="get";
			if(funcList.length<=1){
				break;
			}
			beanFuncName.returnNum = funcList[funcList.length-1];
			beanFuncName.fieldList = new ArrayList<ErayFunBean>();
			for(int j=0;j<funcList.length-1;++j){
				String fieldString = funcList[j];
				functionName+=upperCaseFirstLetter(fieldString);
				ErayFunBean fieldBean = new ErayFunBean();
				fieldBean.field = fieldString;
				if(j==funcList.length-2){
					fieldBean.typeAt = getFieldType(fieldString,data);
					fieldBean.fieldAt = fieldString;
				}else{
					fieldBean.typeAt = getFieldType(fieldString,data)+",";
					fieldBean.fieldAt = fieldString+"&&";
				}
				beanFuncName.fieldList.add(fieldBean);
			}
			beanFuncName.functionName = functionName;
			fieldList.add(beanFuncName);
		}
		createExcelManager(className,fieldList,outPutBasePath);
	}
	private static String upperCaseFirstLetter(String originalLeter){
		return originalLeter.substring(0, 1).toUpperCase()+ originalLeter.substring(1);
	}
//	private static String lowerCaseFirstLetter(String originalLeter){
//		return originalLeter.substring(0, 1).toLowerCase()+ originalLeter.substring(1);
//	}
	
	private static String getFieldType(String fieldStr,String[][] data){
		String [] typeList = data[1];
		String [] fieldList = data[0];
		for(int i = 0;i<fieldList.length;++i){
			if(fieldStr.equals(fieldList[i])){
				return typeList[i];
			}
		}
		return "String";
	}
	public static void  createClassVO(String ClassName,Boolean isAS,Boolean isJava,String[][] data,String outPutBasePath,String defaultPackageString,Boolean isCADD) throws Exception{
		if(isAS||isJava||isCADD){
			ArrayList<ErayBean> fieldList = new ArrayList<ErayBean>();
			String [] d = data[0];
			for(int k=0;k<d.length;k++){
				if(d[k].equals("")){
					continue;
				}
				ErayBean b = new ErayBean();
				for(int i=0;i<3;i++){
					switch(i){
					case 0:
						b.attName = toLowerCaseId(data[i][k]).trim();
						break;
					case 1:
						b.type = upcaseFirst(data[i][k],isAS,isJava).trim();
						b.smallCaseType = (String)data[i][k].toLowerCase().trim();
						break;
					case 2:
						b.commentStr = data[i][k];
						break;
					}
				}
				fieldList.add(b);
			}
			if(isAS){
				createASVOTemplate(ClassName,fieldList,outPutBasePath,defaultPackageString);
			}else{
				//System.out.println("不需生成AS类文件");
				createASVOTemplate(ClassName,fieldList,outPutBasePath,defaultPackageString);
			}
			if(isJava){
				createJavaVOTemplate(ClassName,fieldList,outPutBasePath,defaultPackageString);
			}else{
				//System.out.println("不需生成Java类文件");
			}
			if(isCADD){
				createCADDStruct(ClassName,fieldList,outPutBasePath,defaultPackageString);
			}
		}else{
			//System.out.println("不需生成Java和AS类文件");
		}
	}
	private static void  createASVOTemplate(String ClassName,ArrayList<ErayBean> fieldList,String basePath,String defaultPackageString) throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			try{
				template = freemarkerCfg.getTemplate(cc.getASTemplatePath(),Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", ClassName);
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", "\\")+"\\";
					File asDic = new File(basePath+"DataVO\\as\\"+packageFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+"DataVO\\as\\"+packageFolder+ClassName+"VO.as"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	private static void createExcelManager(String className, ArrayList<ErayFunctionNameBean> fieldList, String basePath) throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			template = freemarkerCfg.getTemplate(cc.getExcelSheetManagerPath(),Locale.ENGLISH);
			template.setEncoding("UTF-8");
			HashMap<Object, Object> root = new HashMap<Object, Object>();
			root.put("ClassName", className);
			root.put("FieldList", fieldList);
			Writer writer;
			try{
				String packageFolder = "managers\\";
				File asDic = new File(basePath+"as3\\"+packageFolder);
				if(!asDic.exists()){
					asDic.mkdirs();
				}
				writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(basePath+"as3\\"+packageFolder+className+".as"), "UTF-8"));
				template.process(root, writer);
				writer.flush();
				writer.close();
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	private static void createCADDStruct(String ClassName,ArrayList<ErayBean> fieldList,String basePath,String defaultPackageString) throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
			try{
				template = freemarkerCfg.getTemplate(cc.getCDDTemplatePath(),Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", ClassName);
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", "\\")+"\\";
					File asDic = new File(basePath+"DataVO\\cadd\\"+packageFolder);
					if(!asDic.exists()){
						asDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+"DataVO\\cadd\\"+packageFolder+ClassName+"VO.h"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	private static String upcaseFirst(String str,Boolean isAS,Boolean isJava){
		if(str.equals("string")){
	        str  = str.substring(0,1).toUpperCase()+str.substring(1);
		}
		if(isAS){
			if(str.equals("long")){
				str = "Number";
			}
		}
        return str;
	}
	private static String toLowerCaseId(String str){
		if(str.equals("ID")||str.equals("Id")||str.equals("iD")){
			str = str.toLowerCase();
		}
		return str;
	}
	private static void createJavaVOTemplate(String ClassName,ArrayList<ErayBean> fieldList,String basePath,String defaultPackageString) throws IOException{
		try{
			BuildConfig cc = new BuildConfig();
			Configuration freemarkerCfg = FreemarkerConfiguration.getConfiguation();
			freemarkerCfg.setDirectoryForTemplateLoading(new File("./"));
			freemarkerCfg.setEncoding(Locale.getDefault(), "gb2312");
			Template template;
//		
			try{
				template = freemarkerCfg.getTemplate(cc.getJavaTemplatePath(),Locale.ENGLISH);
				template.setEncoding("UTF-8");
				HashMap<Object, Object> root = new HashMap<Object, Object>();
				root.put("ClassName", ClassName);
				root.put("FieldList", fieldList);
				Writer writer;
				try{
					String packageFolder = defaultPackageString.replace(".", "\\")+"\\";
					File javaDic = new File(basePath+"DataVO\\java\\"+packageFolder);
					if(!javaDic.exists()){
						javaDic.mkdirs();
					}
					writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(basePath+"DataVO\\java\\"+packageFolder+ClassName+"VO.java"), "UTF-8"));
					template.process(root, writer);
					writer.flush();
					writer.close();
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}catch(Exception e){
				System.out.print(e.toString());
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	
	
}
