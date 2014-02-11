package com.eray.data.vo{
	/**
	 * 毅睿网络
	 * @author Sloppy
	 * 
	 */	
	public class ${ClassName}VO
	{
		public function ${ClassName}VO()
		{
		}
		<#list FieldList as field>
		/**
		 * ${field.commentStr}
		 */		
		private var _${field.attName}:${field.type};

		public function get ${field.attName}():${field.type}
		{
			return _${field.attName};
		}

		public function set ${field.attName}(value:${field.type}):void
		{
			_${field.attName} = value;
		}
		</#list>
		
		public static function parse(obj:Object):${ClassName}VO{
			var o:${ClassName}VO = new ${ClassName}VO();
			<#list FieldList as field>
			o.${field.attName} = obj["${field.attName}"];
			</#list>
			return o;
		}
	}
}