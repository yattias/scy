package listeners
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	public class ToggleEvent extends Event
	{
		public var selecTarget:Number;
		public var direction:String = null;
		public var categoryTitle:String = null;
		public var calculateStack:Boolean = false;
		public var sourcePanel:Number; 
		public var eloidToAssess:String = null; 
		public var portfolioToAssess:String = null; 
		public var elosToCompare:ArrayCollection = null;
		
		public function ToggleEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}