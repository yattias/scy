package utility
{
	import mx.rpc.http.HTTPService;
	
	public final class AutoSaveService
	{
		public var httpService:HTTPService = null;
		
		public function AutoSaveService(serviceURL:String, resFormat:String="object"):void {
			httpService = new HTTPService();
			httpService.method = "POST";
			httpService.resultFormat = resFormat;
			httpService.showBusyCursor = true;
			//httpService.url = serviceURL;
			var myPattern:RegExp = /#/gi; 
			httpService.url = serviceURL.replace(myPattern, "%23");
		}
		
		public function sendTheMofo():void {			
			httpService.send();			
		}
	}
}