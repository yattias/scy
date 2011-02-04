package listeners
{
	import flash.events.Event;
	
	public class ImageMagEvent extends Event
	{
		public var imageSource:String;
		public var showNow:Boolean;
		public var evtType:String;
		public var rtf:String;
		
		public function ImageMagEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}