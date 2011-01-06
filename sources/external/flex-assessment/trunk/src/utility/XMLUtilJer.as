package utility
{
	public class XMLUtilJer
	{
		public static function cdata(data:String):XML {
			return new XML("<![CDATA[" + data + "]]>");
		}
	}
}