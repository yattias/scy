package listeners
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	public class RefreshEvent extends Event
	{
		public var eloXML:XML = null;
		public var eloURI:String = null;
		
		public function RefreshEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}