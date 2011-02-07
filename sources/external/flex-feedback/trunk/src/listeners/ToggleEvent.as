package listeners
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	public class ToggleEvent extends Event
	{
		public var selecTarget:Number;
		public var direction:String = null;
		public var eloToAssess:Object = null; 
		
		public function ToggleEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}