package utils
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

	public class XMLUtilJer
	{
		public static function cdata(data:String):XML {
			return new XML("<![CDATA[" + data + "]]>");
		}
		
		
		public static function getXMLAsEloObject(object:XML):Object {
			var xmlObject:Object;
			xmlObject = new Object();
			xmlObject.uri = object.uri;
			xmlObject.catname = object.catname;
			xmlObject.createdby = object.createdby;
			xmlObject.thumbnail = object.thumbnail;
			xmlObject.fullsize = object.fullsize
			xmlObject.customname = object.customname;
			xmlObject.modified = object.modified;
			xmlObject.studentdescription = object.studentdescription;
			xmlObject.studentglg = getArrayFromXML(object.studentgenerallearninggoals.learninggoal);
			xmlObject.studentslg = getArrayFromXML(object.studentspecificlearninggoals.learninggoal);
			xmlObject.studentreflection = object.studentreflection;
			xmlObject.hasbeenreflectedon = object.hasbeenreflectedon;
			xmlObject.hasbeenselectedforsubmit = object.hasbeenselectedforsubmit;
			xmlObject.inquiryquestion = object.inquiryquestion;
			xmlObject.studentinquiry = object.studentinquiry;
			xmlObject.assessed = object.assessed;
			xmlObject.grade =object.grade;
			xmlObject.assessmentcomment = object.assessmentcomment;
			xmlObject.reflectioncomment = object.reflectioncomment;
			xmlObject.technicalformat = object.technicalformat;
			xmlObject.rawdataid = object.rawdata.id;
			xmlObject.rawdatatext = object.rawdata.text;
			xmlObject.rawdatathumb = object.rawdata.thumbnail;
			xmlObject.rawdatafull = object.rawdata.fullscreen;
			xmlObject.rawdatadataset = object.rawdata.dataset;
			xmlObject.snippeturl = object.snippeturl;
			return xmlObject;
		}
		
		private static function getArrayFromXML(xml:XMLList):ArrayCollection {
			var xmlArray:ArrayCollection = new ArrayCollection();
			
			for each (var object:XML in xml.*) {
				if (object.name() == "goal") {
					xmlArray.addItem([{entry:object.text(), pos:new Number(object.@pos)}]);
				}
			}
			
			return xmlArray;
		}
		
		public static function getSubCanvasEloFromObject(selectedElo:Object):subCanvasElo {
			var subCE:subCanvasElo = new subCanvasElo();
			subCE.uri = selectedElo.uri;
			subCE.catname = selectedElo.catname;
			subCE.createdby = selectedElo.createdby;
			subCE.thumbnail = selectedElo.thumbnail;
			subCE.fullsize = selectedElo.fullsize;
			subCE.customname = selectedElo.customname;
			subCE.modified = selectedElo.modified;
			subCE.studentdescription = selectedElo.studentdescription;
			subCE.studentglg = selectedElo.studentglg;
			subCE.studentslg = selectedElo.studentslg;
			subCE.studentreflection = selectedElo.studentreflection;
			subCE.hasbeenreflectedon = getBooleanValue(selectedElo.hasbeenreflectedon);
			subCE.hasbeenselectedforsubmit = getBooleanValue(selectedElo.hasbeenselectedforsubmit);
			subCE.inquiryquestion = selectedElo.inquiryquestion;
			subCE.studentinquiry = selectedElo.studentinquiry;
			subCE.assessed = selectedElo.assessed;
			subCE.grade =selectedElo.grade;
			subCE.assessmentcomment = selectedElo.assessmentcomment;
			subCE.reflectioncomment = selectedElo.reflectioncomment;
			subCE.highLightELO(subCE.hasbeenselectedforsubmit);
			subCE.technicalformat = selectedElo.technicalformat;
			subCE.rawdataid = selectedElo.rawdataid;
			subCE.rawdatatext = selectedElo.rawdatatext;
			subCE.rawdatathumb = selectedElo.rawdatathumb;
			subCE.rawdatafull = selectedElo.rawdatafull;
			subCE.rawdatadataset = selectedElo.rawdatadataset;
			subCE.snippeturl = selectedElo.snippeturl;
			return subCE;
		}
		
		private static function getBooleanValue(s:String):Boolean {
			if(s == "true") {
				return true;
			}
			else {
				return false;
			}
		}
		
		public static function getXMLFormattedELO(ob:Object):XML {
			var newELO:XML = <elo />;
			var myPattern:RegExp = /#/gi; 
			//			httpService.url = serviceURL.replace(myPattern, "%23");
			
			var uri:String = ob.uri;
			if(uri != null && uri != "") {
				newELO.uri = <uri>{cdata(uri.replace(myPattern, "@"))}</uri>;
			}
			newELO.catname = <catname>{cdata(ob.catname)}</catname>;
			newELO.createdby = <createdby>{cdata(ob.createdby)}</createdby>;
			newELO.thumbnail = <thumbnail>{cdata(ob.thumbnail)}</thumbnail>;
			newELO.fullsize = <fullsize>{cdata(ob.fullsize)}</fullsize>;
			newELO.customname = <customname>{cdata(ob.customname)}</customname>;
			newELO.modified = <modified>{cdata(ob.modified)}</modified>;
			newELO.studentdescription = <studentdescription>{cdata(ob.studentdescription)}</studentdescription>;
			newELO.studentgenerallearninggoals = <studentgenerallearninggoals />;
			for(var i:Number = 0; i<ob.slectedGLGs.length; i++) {
				var learninggoal:XML = <learninggoal />;
				var goal:XML = <goal>{cdata(ob.slectedGLGs[i][0].entry)}</goal>;
				goal.@pos = ob.slectedGLGs[i][0].pos;
				learninggoal.appendChild(goal);
				newELO.studentgenerallearninggoals.appendChild(learninggoal);
			}
			newELO.studentspecificlearninggoals = <studentspecificlearninggoals />;
			for(var i:Number = 0; i<ob.slectedSLGs.length; i++) {
				var learninggoal:XML = <learninggoal />;
				var goal:XML = <goal>{cdata(ob.slectedSLGs[i][0].entry)}</goal>;
				goal.@pos = ob.slectedSLGs[i][0].pos;
				learninggoal.appendChild(goal);
				newELO.studentspecificlearninggoals.appendChild(learninggoal);
			}
			newELO.studentreflection = <studentreflection>{cdata(ob.studentreflection)}</studentreflection>;
			newELO.hasbeenreflectedon = <hasbeenreflectedon>{ob.hasbeenreflectedon}</hasbeenreflectedon>;
			newELO.hasbeenselectedforsubmit = <hasbeenselectedforsubmit>{ob.hasbeenselectedforsubmit}</hasbeenselectedforsubmit>;
			newELO.technicalformat = <technicalformat>{cdata(ob.technicalformat)}</technicalformat>;
			newELO.rawdata = <rawdata />;
			newELO.rawdata.id = <id>{cdata(ob.rawdataid)}</id>;
			var rawdatatext:String = ob.rawdatatext;
			if(rawdatatext != null && rawdatatext != "") {
				newELO.rawdata.text = <text>{cdata((cleanHTML(rawdatatext)).replace(myPattern, "@"))}</text>;
			}
			var rawdatathumb:String = ob.rawdatathumb;
			if(rawdatathumb != null && rawdatathumb != "") {
				newELO.rawdata.thumbnail = <thumbnail>{cdata((rawdatathumb).replace(myPattern, "@"))}</thumbnail>;
			}
			var rawdatafull:String = ob.rawdatafull;
			if(rawdatafull != null && rawdatafull != "") {
				newELO.rawdata.fullscreen = <fullscreen>{cdata((rawdatafull).replace(myPattern, "@"))}</fullscreen>;	
			}
			var rawdatadataset:String = ob.rawdatadataset;
			if(rawdatadataset != null && rawdatadataset != "") {
				newELO.rawdata.dataset = <dataset>{cdata((rawdatadataset).replace(myPattern, "@"))}</dataset>;
			}
			newELO.snippeturl = <snippeturl>{cdata(ob.snippeturl)}</snippeturl>;
			newELO.inquiryquestion = <inquiryquestion>{cdata(ob.inquiryquestion)}</inquiryquestion>;
			newELO.studentinquiry = <studentinquiry>{ob.studentinquiry}</studentinquiry>;
			newELO.assessed = <assessed>false</assessed>;
			newELO.grade = <grade />;
			newELO.assessmentcomment = <assessmentcomment />;
			newELO.reflectioncomment = <reflectioncomment />;
			
			return newELO;
		}
		
		
		public static function getXMLActionLoggerObject(meta:Dictionary, attribs:ArrayCollection):XML {
			var XMLaction:XML = <action />;
			XMLaction.tool = <tool>{cdata(meta["tool"])}</tool>;
			XMLaction.type = <type>{cdata(meta["type"])}</type>;
			XMLaction.elouri = <elouri>{cdata(meta["elouri"])}</elouri>;
			XMLaction.attributes = <attributes />;
			
			for each (var object:Object in attribs) {
				var attr:XML = <attribute />;
				attr.name = <name>{cdata(object.oName)}</name>;
				attr.value = <value>{cdata(object.oVal)}</value>;
				XMLaction.attributes.appendChild(attr);
			}
			
			return XMLaction;
		}
		
		public static function cleanHTML(s:String):String {
			var myPattern:RegExp = /<.+>/gi; 
			s = s.replace(myPattern, "");
			myPattern = /\t\n/gi;
			s = s.replace(myPattern, "");
			myPattern = /&amp;#xD;/gi; 
			s = s.replace(myPattern, "");
			return s;
		}
	}
}