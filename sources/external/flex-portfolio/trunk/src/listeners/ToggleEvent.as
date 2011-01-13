package listeners
{
	import flash.events.Event;
	
	public class ToggleEvent extends Event
	{
		public var selecTarget:Number;
		public var direction:String;
		public var editReflection:Boolean = false;
		public var newSearch:Boolean = true;
		
		public function ToggleEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}