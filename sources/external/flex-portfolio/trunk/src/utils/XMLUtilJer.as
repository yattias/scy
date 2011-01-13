package utils
{
	import mx.collections.ArrayCollection;

	public class XMLUtilJer
	{
		public static function cdata(data:String):XML {
			return new XML("<![CDATA[" + data + "]]>");
		}
		
		
		public static function getEloObjectAsXML(object:XML):Object {
			var xmlObject:Object;
			xmlObject = new Object();
			xmlObject.uri = object.@uri;
			xmlObject.catname = object.catname;
			xmlObject.createdby = object.createdby;
			xmlObject.thumbnail = object.thumbnail;
			xmlObject.fullsize = object.fullsize
			xmlObject.customname = object.customname;
			xmlObject.modified = object.modified;
			xmlObject.studentDescription = object.studentDescription;
			xmlObject.studentGLG = getArrayFromXML(object.studentGLG);
			xmlObject.studentSLG = getArrayFromXML(object.studentSLG);
			xmlObject.studentReflection = object.studentReflection;
			xmlObject.hasBeenReflectedOn = object.hasBeenReflectedOn;
			xmlObject.hasBeenSelectedForSubmit = object.hasBeenSelectedForSubmit;
			xmlObject.inquiryQuestion = object.inquiryQuestion;
			xmlObject.studentInquiry = object.studentInquiry;
			xmlObject.assessed = object.assessed;
			xmlObject.grade =object.grade;
			xmlObject.assessmentComment = object.assessmentComment;
			xmlObject.reflectionComment = object.reflectionComment;
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
			subCE.studentDescription = selectedElo.studentDescription;
			subCE.studentGLG = selectedElo.studentGLG;
			subCE.studentSLG = selectedElo.studentSLG;
			subCE.studentReflection = selectedElo.studentReflection;
			subCE.hasBeenReflectedOn = getBooleanValue(selectedElo.hasBeenReflectedOn);
			subCE.hasBeenSelectedForSubmit = getBooleanValue(selectedElo.hasBeenSelectedForSubmit);
			subCE.inquiryQuestion = selectedElo.inquiryQuestion;
			subCE.studentInquiry = selectedElo.studentInquiry;
			subCE.assessed = selectedElo.assessed;
			subCE.grade =selectedElo.grade;
			subCE.assessmentComment = selectedElo.assessmentComment;
			subCE.reflectionComment = selectedElo.reflectionComment;
			subCE.highLightELO(subCE.hasBeenSelectedForSubmit);
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
			newELO.@uri = ob.uri;
			newELO.catname = <catname>{cdata(ob.catname)}</catname>;
			newELO.createdby = <createdby>{cdata(ob.createdby)}</createdby>;
			newELO.thumbnail = <thumbnail>{cdata(ob.thumbnail)}</thumbnail>;
			newELO.fullsize = <fullsize>{cdata(ob.fullsize)}</fullsize>;
			newELO.customname = <myname>{cdata(ob.customname)}</myname>;
			newELO.modified = <modified>{cdata(ob.modified)}</modified>;
			newELO.studentDescription = <studentDescription>{cdata(ob.studentDescription)}</studentDescription>;
			newELO.studentGLG = <studentGLG />;
			for(var i:Number = 0; i<ob.slectedGLGs.length; i++) {
				var goal:XML = <goal>{cdata(ob.slectedGLGs[i][0].entry)}</goal>
				goal.@pos = ob.slectedGLGs[i][0].pos;
				newELO.studentGLG.appendChild(goal);
			}
			newELO.studentSLG = <studentSLG />;
			for(var i:Number = 0; i<ob.slectedSLGs.length; i++) {
				var goal:XML = <goal>{cdata(ob.slectedSLGs[i][0].entry)}</goal>;
				goal.@pos = ob.slectedSLGs[i][0].pos;
				newELO.studentSLG.appendChild(goal);
			}
			newELO.studentReflection = <studentReflection>{cdata(ob.studentReflection)}</studentReflection>;
			newELO.hasBeenReflectedOn = <hasBeenReflectedOn>{ob.hasBeenReflectedOn}</hasBeenReflectedOn>;
			newELO.hasBeenSelectedForSubmit = <hasBeenSelectedForSubmit>{ob.hasBeenSelectedForSubmit}</hasBeenSelectedForSubmit>;
			newELO.inquiryQuestion = <inquiryQuestion>{cdata(ob.inquiryQuestion)}</inquiryQuestion>;
			newELO.studentInquiry = <studentInquiry>{ob.studentInquiry}</studentInquiry>;
			newELO.assessed = <assessed>false</assessed>;
			newELO.grade = <grade />;
			newELO.assessmentComment = <assessmentComment />;
			newELO.reflectionComment = <reflectionComment />;
			
			return newELO;
		}
	}
}