package utility
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

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
			subCE.shouldBeSelected = selectedElo.shouldBeSelected;
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
	}
}