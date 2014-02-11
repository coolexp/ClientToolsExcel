package com.eray.data.vo;
	/**
	 * 毅睿网络
	 * @author Sloppy
	 * 
	 */	
	public class ${ClassName}VO {
		<#list FieldList as field>
		/**
		 * ${field.commentStr}
		 */	
		public ${field.type} ${field.attName};
		</#list>
}
