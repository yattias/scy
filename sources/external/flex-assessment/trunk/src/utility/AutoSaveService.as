package utility
{
	import mx.rpc.http.HTTPService;

	public final class AutoSaveService
	{
		private var httpService:HTTPService = null;
		
		public function AutoSaveService(serviceURL:String):void {
			httpService = new HTTPService();
			httpService.method = "POST";
			httpService.showBusyCursor = true;
			httpService.resultFormat = "e4x";
			httpService.url = serviceURL;
			httpService.send();			
		}
	}
}