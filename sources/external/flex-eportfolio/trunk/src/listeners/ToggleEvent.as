package listeners
{
	import flash.events.Event;
	
	public class ToggleEvent extends Event
	{
		public var selecTarget:Number;
		public var direction:String;
		public var eloToAdd:Boolean;
		public var eloSelectedToSubmit:Boolean = false;
		public var accordionTarget:Number = Main.STACK_INTRO_PANEL;
		
		public function ToggleEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}