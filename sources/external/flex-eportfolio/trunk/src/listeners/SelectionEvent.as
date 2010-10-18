package listeners
{
	import flash.events.Event;
	
	public class SelectionEvent extends Event
	{
		public function SelectionEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}