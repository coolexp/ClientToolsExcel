package com.eray.base.utils.excel.managers
{
	public class DotaSheetsNameData
	{
		public function DotaSheetsNameData()
		{
		}
		
<#list FieldList as field>	
		public static const ${field.uuString}:String = "${field.staticString}";
</#list>
	}
}