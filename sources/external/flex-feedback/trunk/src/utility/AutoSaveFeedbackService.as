package utility
{
	import listeners.RefreshEvent;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import flash.events.Event;
	import flash.events.EventDispatcher;

	public final class AutoSaveFeedbackService
	{
		public var httpService:HTTPService = null;
		
		public function AutoSaveFeedbackService(serviceURL:String):void {
			httpService = new HTTPService();
			httpService.method = "POST";
			httpService.showBusyCursor = true;
			httpService.resultFormat = "e4x";
			
			var myPattern:RegExp = /#/gi; 
			httpService.url = serviceURL.replace(myPattern, "%23");
			httpService.send();			
		}
	}
}