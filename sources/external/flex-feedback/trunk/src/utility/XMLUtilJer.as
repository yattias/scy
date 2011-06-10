package utility
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.utils.UIDUtil;

	public class XMLUtilJer
	{
		public static function cdata(data:String):XML {
			return new XML("<![CDATA[" + data + "]]>");
		}
		
		
		public static function getEloObjectFromXML(object:XML):Object {
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
			xmlObject.studentglg = getArrayFromXML(object.studentglg);
			xmlObject.studentslg = getArrayFromXML(object.studentslg);
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
			xmlObject.shown = object.shown;
			xmlObject.evalu = object.evalu;
			xmlObject.score = object.score;
			xmlObject.feedbackelourl = object.feedbackelourl;
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
		
		public static function getGoalString(goalXML:XMLList):String {
			var goals:String = "";
			for each (var object:XML in goalXML.*) {
				if (object.name() == "goal") {
					goals = goals + "- " + object.text() + "\n";
				}
			}
			
			return goals;
		}
		
		public static function getTileCellEloFromObject(selectedElo:Object):tileCell {
			var subCE:tileCell = new tileCell();
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
			subCE.shouldBeSelected = selectedElo.shouldBeSelected;
			subCE.technicalformat = selectedElo.technicalformat;
			subCE.rawdataid = selectedElo.rawdataid;
			subCE.rawdatatext = selectedElo.rawdatatext;
			subCE.rawdatathumb = selectedElo.rawdatathumb;
			subCE.rawdatafull = selectedElo.rawdatafull;
			subCE.rawdatadataset = selectedElo.rawdatadataset;
			subCE.shown = selectedElo.shown;
			subCE.evalu = selectedElo.evalu;
			subCE.score = selectedElo.score;
			subCE.feedbackelourl = selectedElo.feedbackelourl;
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
			newELO.uri = <uri>{cdata((ob.uri).replace("#", "%23"))}</uri>;
			newELO.catname = <catname>{cdata(ob.catname)}</catname>;
			newELO.createdby = <createdby>{cdata(ob.createdby)}</createdby>;
			newELO.thumbnail = <thumbnail>{cdata(ob.thumbnail)}</thumbnail>;
			newELO.fullsize = <fullsize>{cdata(ob.fullsize)}</fullsize>;
			newELO.customname = <customname>{cdata(ob.customname)}</customname>;
			newELO.modified = <modified>{cdata(ob.modified)}</modified>;
			newELO.studentdescription = <studentdescription>{cdata(ob.studentdescription)}</studentdescription>;
			newELO.studentglg = <studentglg />;
			for(var i:Number = 0; i<ob.slectedGLGs.length; i++) {
				var goal:XML = <goal>{cdata(ob.slectedGLGs[i][0].entry)}</goal>
				goal.@pos = ob.slectedGLGs[i][0].pos;
				newELO.studentglg.appendChild(goal);
			}
			newELO.studentslg = <studentslg />;
			for(var i:Number = 0; i<ob.slectedSLGs.length; i++) {
				var goal:XML = <goal>{cdata(ob.slectedSLGs[i][0].entry)}</goal>;
				goal.@pos = ob.slectedSLGs[i][0].pos;
				newELO.studentslg.appendChild(goal);
			}
			newELO.studentreflection = <studentreflection>{cdata(ob.studentreflection)}</studentreflection>;
			newELO.hasbeenreflectedon = <hasbeenreflectedon>{ob.hasbeenreflectedon}</hasbeenreflectedon>;
			newELO.hasbeenselectedforsubmit = <hasbeenselectedforsubmit>{ob.hasbeenselectedforsubmit}</hasbeenselectedforsubmit>;
			newELO.technicalformat = <technicalformat>{cdata(ob.technicalformat)}</technicalformat>;
			newELO.rawdata = <rawdata />;
			newELO.rawdata.id = <id>{cdata(ob.rawdataid)}</id>;
			newELO.rawdata.text = <text>{cdata(ob.rawdatatext)}</text>;
			newELO.rawdata.thumbnail = <thumbnail>{cdata(ob.rawdatathumb)}</thumbnail>;
			newELO.rawdata.fullscreen = <fullscreen>{cdata(ob.rawdatafull)}</fullscreen>;
			newELO.rawdata.dataset = <dataset>{cdata(ob.rawdatadataset)}</dataset>;
			//newELO.shown = <shown>{cdata(ob.shown)}</shown>;
			newELO.evalu = <evalu>{cdata(ob.evalu)}</evalu>;
			//newELO.score = <score>{cdata(ob.score)}</score>;
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
		
		public static function getFeedBackXML(dic:Dictionary):XML {
			var my_date:Date = new Date();
			var uuid:String = UIDUtil.createUID(); 
			var XMLfeed:XML = <feedback />;
			XMLfeed.id = <id>{uuid}</id>
			XMLfeed.createdby = <createdby>{cdata(dic["author"])}</createdby>;
			XMLfeed.createdbypicture = <createdbypicture />;
			XMLfeed.calendardate = <calendardate>{cdata(getFormattedDate(my_date))}</calendardate>;
			XMLfeed.calendartime = <calendartime>{cdata(getFormattedTime(my_date))}</calendartime>;
			XMLfeed.comment = <comment>{cdata(dic["comment"])}</comment>;
			XMLfeed.evalu = <evalu>{cdata(dic["evalu"])}</evalu>;
			XMLfeed.replies = <replies />;
			
			return XMLfeed;
		}
		
		public static function getReplyFeedBackXML(dic:Dictionary):XML {
			var my_date:Date = new Date();
			var XMLfeed:XML = <reply />;
			XMLfeed.createdby = <createdby>{cdata(dic["author"])}</createdby>;
			XMLfeed.createdbypicture = <createdbypicture />;
			XMLfeed.calendardate = <calendardate>{cdata(getFormattedDate(my_date))}</calendardate>;
			XMLfeed.calendartime = <calendartime>{cdata(getFormattedTime(my_date))}</calendartime>;
			XMLfeed.comment = <comment>{cdata(dic["comment"])}</comment>;
			
			return XMLfeed;
		}
		
		private static function getFormattedDate(my_date:Date):String {
			return getFullDigit(my_date.date.toString()) + "/" + getFullDigit((my_date.month+1).toString()) + "/" + getFullDigit(my_date.fullYear.toString());
		}
		
		private static function getFormattedTime(my_date:Date):String {
			return getFullDigit(my_date.hours.toString()) + ":" + getFullDigit(my_date.minutes.toString()) + ":" + getFullDigit(my_date.seconds.toString());
		}
		
		private static function getFullDigit(s:String):String {
			if(s.length == 1) {
				return "0"+s;
			}
			else {
				return s;
			}
		}
	}
}