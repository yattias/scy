package listeners
{
	import flash.events.Event;
	
	public class SelectCompareEvent extends Event
	{
		public var clickedElo:subCanvasElo;
		public var cbSelected:Boolean;
		
		public function SelectCompareEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}