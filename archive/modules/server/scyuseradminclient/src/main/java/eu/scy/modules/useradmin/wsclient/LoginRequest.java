
package eu.scy.modules.useradmin.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="UserName" type="{http://www.scy-net.eu/schemas}StringType"/>
 *         &lt;element name="Password" type="{http://www.scy-net.eu/schemas}StringType"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "LoginRequest")
public class LoginRequest {

    @XmlElement(name = "UserName", required = true)
    protected StringType userName;
    @XmlElement(name = "Password", required = true)
    protected StringType password;

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link StringType }
     *     
     */
    public StringType getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringType }
     *     
     */
    public void setUserName(StringType value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link StringType }
     *     
     */
    public StringType getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringType }
     *     
     */
    public void setPassword(StringType value) {
        this.password = value;
    }

}
