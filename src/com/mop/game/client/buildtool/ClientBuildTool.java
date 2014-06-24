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
		tfe.tranformPath(iao.path,iao.isTrue);
		System.out.println("build is ok   Spend time mills:  " + (System.currentTimeMillis() - s));
		System.out.println("Path:"+iao.outPrePath);
		System.out.println("PackExcelSuccess");
		System.exit(0);
	}

}
