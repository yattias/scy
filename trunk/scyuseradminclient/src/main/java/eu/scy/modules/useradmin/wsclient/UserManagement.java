
package eu.scy.modules.useradmin.wsclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.1-02/02/2007 03:56 AM(vivekp)-FCS
 * Generated source version: 2.1
 * 
 */
@WebService(name = "UserManagement", targetNamespace = "http://www.scy-net.eu/schemas")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UserManagement {


    /**
     * 
     * @param loginRequest
     * @return
     *     returns eu.scy.modules.useradmin.wsclient.LoginResponse
     */
    @WebMethod(operationName = "Login")
    @WebResult(name = "LoginResponse", targetNamespace = "http://www.scy-net.eu/schemas", partName = "LoginResponse")
    public LoginResponse login(
        @WebParam(name = "LoginRequest", targetNamespace = "http://www.scy-net.eu/schemas", partName = "LoginRequest")
        LoginRequest loginRequest);

}
