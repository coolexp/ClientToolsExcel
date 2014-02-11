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
