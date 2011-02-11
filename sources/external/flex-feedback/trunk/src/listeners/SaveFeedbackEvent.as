package listeners
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	public class SaveFeedbackEvent extends Event
	{
		public function SaveFeedbackEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}