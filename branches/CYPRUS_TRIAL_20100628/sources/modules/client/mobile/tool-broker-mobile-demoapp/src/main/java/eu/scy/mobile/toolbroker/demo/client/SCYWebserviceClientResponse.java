package eu.scy.mobile.toolbroker.demo.client;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 31.mar.2009
 * Time: 15:46:53
 * To change this template use File | Settings | File Templates.
 */
public class SCYWebserviceClientResponse {
	public final static int RESPONSE_FAULT = -1;
	public final static int RESPONSE_SUCCESS = 1;

	private String message;
	private int code;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String toString() {
		return "SCY Server response #" + getCode() + ": " + getMessage();
	}

}
