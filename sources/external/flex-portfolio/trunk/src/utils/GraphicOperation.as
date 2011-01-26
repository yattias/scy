package utils
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	
	import mx.core.INavigatorContent;

	public class GraphicOperation
	{
		public static function getBitmap( target:INavigatorContent ) : Bitmap
		{
			return new Bitmap(getBitmapData(target));
		}
		
		public static function getBitmapData( target:INavigatorContent ) : BitmapData
		{
			var bd:BitmapData = new BitmapData(target.width,target.height);
			bd.draw(target);
			return bd;
		}
	}
}