package data
{
	import core.utils.ObjectPool;
	
	import flash.utils.Dictionary;
	
	/**
	 * 文件名：${ClassName}Data.as
	 * <p>
	 * 功能：
	 * <p>
	 * 版本：1.0.0
	 * <p>
	 * 日期：${Date}
	 * <p>
	 * 作者：Builded by ClientBuildTool.
	 * <p>
	 * 版权：(c)千橡游戏
	 */
	public class ${ClassName}Data
	{
		public static var dataList:Dictionary = ObjectPool.borrow(Dictionary);
		
		public static function init():void{
		<#list FieldList as field>
			dataList[${field.id}] = ${field.data};			
		</#list>
		}
		
		public function ${ClassName}Data()
		{
		}
	}
}

