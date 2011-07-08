//package utility
//{
//	import mx.rpc.http.HTTPService;
//	
//	public final class AutoSaveService
//	{
//		public var httpService:HTTPService = null;
//		
//		public function AutoSaveService(serviceURL:String, resFormat:String="object"):void {
//			httpService = new HTTPService();
//			httpService.method = "POST";
//			httpService.resultFormat = resFormat;
//			httpService.showBusyCursor = true;
//			//httpService.url = serviceURL;
//			var myPattern:RegExp = /#/gi; 
//			httpService.url = serviceURL.replace(myPattern, "%23");
//		}
//		
//		public function sendTheMofo():void {			
//			httpService.send();			
//		}
//	}
//}

package utility
{
	import mx.rpc.http.HTTPService;
	
	public final class AutoSaveService {
		
		public var httpService:HTTPService = null;
		private var params:Object;
		
		public function AutoSaveService(serviceURL:String, feedbackURI:String = null, xmlContent:String = null, logaction:String = null, replyfeedbackID:String=null):void {
			httpService = new HTTPService();
			httpService.method = "POST";
			httpService.showBusyCursor = true;
			httpService.resultFormat = "e4x";
			httpService.url = serviceURL;
			params = new Object();
			
			if(feedbackURI != null && feedbackURI != "") {
				params.feedbackURI = feedbackURI.replace("#", "@");
			}
			if(replyfeedbackID != null && replyfeedbackID != "") {
				params.replyfeedbackID = replyfeedbackID.replace("#", "@");
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