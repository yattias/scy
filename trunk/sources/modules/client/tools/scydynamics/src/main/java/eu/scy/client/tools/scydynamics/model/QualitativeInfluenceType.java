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
	ASYMPTOTE_UP("asymptote_up");

	private final String value;

	private QualitativeInfluenceType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static QualitativeInfluenceType fromValue(String v) {
		for (QualitativeInfluenceType c : QualitativeInfluenceType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}