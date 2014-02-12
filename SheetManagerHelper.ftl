package com.eray.base.utils.excel
{
	import com.eray.base.debug.Console;
	import com.eray.base.utils.excel.managers.DotaSheetsNameData;
	
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
<#list FieldList as field>
	import com.eray.base.utils.excel.managers.${field.claString};
</#list>
	public class ErayExcelDataHelper extends EventDispatcher
	{
		private static var _instance:ErayExcelDataHelper;
		private var dic:Dictionary;
		public function ErayExcelDataHelper(target:IEventDispatcher=null)
		{
			super(target);
		}
		/**
		 * 获取Excel表格处理总类单例 
		 * @return 
		 * 
		 */		
		public static function getInstance():ErayExcelDataHelper{
			if(_instance==null){
				_instance = new ErayExcelDataHelper();
			}
			return _instance;
		}
		/**
		 * 初始化表格数据 
		 * @param ba 总表格二进制数据
		 * 
		 */		
		public function init(ba:ByteArray):void{
			if(ba){
				dic = new Dictionary();
				ba.position = 0;
				ba.uncompress();
				var obj:Object = ba.readObject();
				for (var key:String in obj){
					dic[key] = obj[key];
				}
			}
		}
		/**
		 * 设置Excel整个表格数据 
		 * @param excelName
		 * @param obj
		 * 
		 */		
		public function setExcelDataByName(excelName:String,obj:Object):void{
			if(obj is ByteArray){
				var ba:ByteArray = obj as ByteArray;
				ba.position = 0;
				ba.uncompress();
				var o:Object = ba.readObject();
				dic[excelName] = o;
				fillSheetsData(o);
			}
			if(dic[excelName]==null){
				dic[excelName] = obj;
				fillSheetsData(obj);
			}else{
				throw new Error("已经有了表格数据了:"+excelName);
			}
		}
		/**
		 * 循环填充Sheets数据 
		 * @param data
		 * 
		 */		
		private function fillSheetsData(data:Object):void{
			for (var key:String in data){
				dic[key] = data[key];
			}
		}
		/**
		 * 通过表格名获取表格数据 
		 * @param sheetsName
		 * @return 
		 * 
		 */		
		private function getSheetsDataByName(sheetsName:String):Object{
			var obj:Object = dic[sheetsName];
			if(!obj){
				Console.log("表格数据不存在"+sheetsName);
				return null;
			}
			return obj;
		}
<#list FieldList as field>	
		public function get${field.claString}():${field.claString}{
			var ${field.claString} = null;
			do{
				var obj:Object = this.getSheetsDataByName(DotaSheetsNameData.${field.uuString});
				if(obj==null){
					break;
				}
				manager = new ${field.claString}(obj);
			}while(0);
			return manager;
		}
</#list>
		
	}
}