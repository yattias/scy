package listeners
{
	import flash.events.Event;
	
	public class DeleteEvent extends Event
	{
		public var selecIndex:Number;
		
		public function DeleteEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}