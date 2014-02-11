package com.mop.game.client.buildtool;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.coolexp.vo.InputArgsVO;
import com.mop.game.client.buildtool.util.TranformExcel;

public class ClientBuildTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		InputArgsVO iao = InputArgsVO.parse(args);
		long s = System.currentTimeMillis();
		File fil = new File("export");
		if (fil.exists()){
			FileUtils.deleteDirectory(fil);
		}
		TranformExcel tfe = new TranformExcel();
		tfe.setInputArgsVO(iao);
		tfe.setOutputFilePath(iao.outPrePath,iao.defaultPackagePath,iao.isOutPutSheetsDat);
		tfe.tranformPath(iao.path,iao.isTrue,iao.isData,iao.outAS,iao.outJava,iao.outCADD);
		System.out.println("build is ok   Spend time mills:  " + (System.currentTimeMillis() - s));
		System.out.println("Path:"+iao.outPrePath);
		System.exit(0);
		
	}

}
