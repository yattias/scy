package utils
{
	import mx.rpc.http.HTTPService;

	public final class AutoSaveService
	{
		public var httpService:HTTPService = null;
		
		public function AutoSaveService(serviceURL:String):void {
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