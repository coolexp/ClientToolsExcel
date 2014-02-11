package com.eray.base.utils.excel.managers{
	/**
	 * 毅睿网络
	 * @author Sloppy
	 * 
	 */	
	public class ${ClassName}Manager extends BaseSheetsManager
	{
		public function ${ClassName}Manager(obj:Object)
		{
			super(obj);
		}
		<#list FieldList as field>
		public function ${field.functionName}(<#list field.fieldList as itemField>${itemField.field}:${itemField.typeAt}</#list>):<#if field.returnNum != "1" >Array<#else>Object</#if>{
			if(mSheetsObj!=null){
				var list:Array = [];
				for each(var node:Object in mSheetsObj){
					if(<#list field.fieldList as itemNField>node["${itemNField.field}"]==${itemNField.fieldAt}</#list>){
						list.push(node);
					}
				}
				<#if field.returnNum != "1" >
				return list;
				<#else>
				if(list.length==1){
					return list[0];
				}else{
					trace("数据超过一条,不唯一");
					return null;
				}</#if>
			}
			return null;
		}
		</#list>
		/**
		 * 释放类方法 
		 * 
		 */	
		override public function dispose():void
		{
			// TODO Auto Generated method stub
			//code here
			super.dispose();
		}
		
	}
}