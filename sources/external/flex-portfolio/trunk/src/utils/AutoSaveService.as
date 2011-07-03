package utils
{
	import mx.rpc.http.HTTPService;

	public final class AutoSaveService {
		
		public var httpService:HTTPService = null;
		private var params:Object;
		
		public function AutoSaveService(serviceURL:String, missionURI:String = null, xmlContent:String = null, logaction:String = null):void {
			httpService = new HTTPService();
			httpService.method = "POST";
			httpService.showBusyCursor = true;
			httpService.url = serviceURL;
			params = new Object();
			if(missionURI != null && missionURI != "") {
				params.missionURI = missionURI.replace("#", "@");
			}
			if(xmlContent != null && xmlContent != "") {
				//params.xmlContent = escape(xmlContent);
				params.xmlContent = xmlContent;
			}
			if(logaction != null && logaction != "") {
				//params.logaction = escape(logaction);
				params.logaction = logaction;
			}
		}
		
		public function sendTheMofo():void {			
			httpService.send(params);			
		}
	}
}