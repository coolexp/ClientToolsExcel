package com.mop.game.client.buildtool.config;

import com.mop.game.client.buildtool.util.OtherUtils;

public class BuildConfig {
	public String currentPath = OtherUtils.getCurrentPath();
	public String configExcelPath = currentPath + "config";
	public String templateASPath = "ASDataVO.ftl";
	public String templateJAVAPath = "JAVADataVO.ftl";
	public String templateCDDPath = "CADD.ftl";
	public String templateFacPath = "VOFactory.ftl";
	public String templateExcelManagePath = "SheetManager.ftl";
	public String templateExcelHelperPath = "SheetManagerHelper.ftl";
	public String templateExcelHelperStrPath = "SheetStringHelper.ftl";
	
	private String outputfile = "ErayClientData.xml";
	private String outputDatafile = "ErayClientData.dat";
	private String outputDatafileAmf = "ErayClientData.amf";
	private String outputXMLFile = "ErayClassObj.xml";
	
	

	private  String basePath="";
	private  String defaultPackageString;
	
	public String getBasePath(){
		return basePath;
	}
	public String getPackageString(){
		return defaultPackageString;
	}
	
	
	public void initOutPutInfo(String path,String packString){
		outputfile = path + outputfile;
		outputDatafile = path + outputDatafile;
		outputDatafileAmf = path + outputDatafileAmf;
		outputXMLFile = path + outputXMLFile;
		defaultPackageString = packString;
		basePath = path;
	}
	public String getOutPutFile(){
		return outputfile;
	}
	public String getOutputDatafile(){
		return outputDatafile;
	}
	public String getOutputDatafileAmf(){
		return outputDatafileAmf;
	}
	public String getOutputXMLFile(){
		return outputXMLFile;
	}
	
	private static BuildConfig _instance;
	public static BuildConfig getInstance(){
		if(_instance==null){
			_instance = new BuildConfig();
		}
		return _instance;
	}
	public static void disposeInstance(){
		_instance = null;
	}
	public String getExcelStringHelperPath(){
		return templateExcelHelperStrPath;
	}
	public String getExcelSheetHelperPath(){
		return templateExcelHelperPath;
	}
	
	public String getExcelSheetManagerPath(){
		return templateExcelManagePath;
	}
	public String getConfigExcelPath() {
		return configExcelPath;
	}
	
	public void setConfigExcelPath(String configXmlPath) {
		this.configExcelPath = configXmlPath;
	}
	public String getASTemplatePath(){
		return templateASPath;
	}
	public String getCDDTemplatePath(){
		return templateCDDPath;
	}
	public String getJavaTemplatePath(){
		return templateJAVAPath;
	}
	public String getFacTemplatePath(){
		return templateFacPath;
	}
}
