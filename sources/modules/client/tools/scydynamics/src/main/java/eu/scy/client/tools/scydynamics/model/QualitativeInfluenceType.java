package eu.scy.client.tools.scydynamics.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "type")
@XmlEnum
public enum QualitativeInfluenceType {

	@XmlEnumValue("unspecified")
	UNSPECIFIED("unspecified"),
	
	@XmlEnumValue("linear_up")
	LINEAR_UP("linear_up"),

	@XmlEnumValue("linear_down")
	LINEAR_DOWN("linear_down"),
	
	@XmlEnumValue("curve_up")
	CURVE_UP("curve_up"),
	
	@XmlEnumValue("curve_down")
	CURVE_DOWN("curve_down"),
	
	@XmlEnumValue("asymptote_up")
	ASYMPTOTE_UP("asymptote_up"),
	
	@XmlEnumValue("s_shaped")
	S_SHAPED("s_shaped"),
	
	@XmlEnumValue("constant")
	CONSTANT("constant"),
	
	@XmlEnumValue("bell")
	BELL("bell");

	private final String value;

	private QualitativeInfluenceType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static QualitativeInfluenceType fromValue(String v) {
		for (QualitativeInfluenceType c : QualitativeInfluenceType.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
	
	public static int getInt(QualitativeInfluenceType inputType) {
		int returnType;
		switch (inputType) {
			case LINEAR_UP: returnType = 1; break;
			case CURVE_DOWN: returnType = 2; break;
			case LINEAR_DOWN: returnType = 3; break;
			case CURVE_UP: returnType = 4; break;
			case ASYMPTOTE_UP: returnType = 5; break;
			case UNSPECIFIED: returnType = 6; break;
			case S_SHAPED: returnType = 8; break;
			case CONSTANT: returnType = 9; break;
			case BELL: returnType = 10; break;
			default: throw new IllegalArgumentException(""+inputType);
		}
	return returnType;
	}
	
	public static QualitativeInfluenceType fromInt(int inputType) {
		QualitativeInfluenceType returnType;
		switch (inputType) {
			case 0: returnType = QualitativeInfluenceType.UNSPECIFIED; break;
			case 1: returnType = QualitativeInfluenceType.LINEAR_UP; break;
			case 2: returnType = QualitativeInfluenceType.CURVE_DOWN; break;
			case 3: returnType = QualitativeInfluenceType.LINEAR_DOWN; break;
			case 4: returnType = QualitativeInfluenceType.CURVE_UP; break;
			case 5: returnType = QualitativeInfluenceType.ASYMPTOTE_UP; break;
			case 6: returnType = QualitativeInfluenceType.UNSPECIFIED; break;
			case 8: returnType = QualitativeInfluenceType.S_SHAPED; break;
			case 9: returnType = QualitativeInfluenceType.CONSTANT; break;
			case 10: returnType = QualitativeInfluenceType.BELL; break;
			default: throw new IllegalArgumentException(""+inputType);
		}
		return returnType;
	}
}