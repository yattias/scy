/*
 * Created on 09.nov.2004
 *
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;
import java.net.InetAddress;

import eu.scy.colemo.network.Person;

/**
 *
 */
public class AddClass extends BaseConceptMapNode implements Serializable,Contribution{

	private String name;
	private String type;
	private String author;
	private InetAddress ip;
	private Person person;
	private int x=100;
	private int y=100;
	
	public AddClass(String name,String type,String author,InetAddress ip,Person person) {
		this.name=name;
		this.type=type;
		this.author=author;
		this.person=person;
		this.ip=ip;
	}

	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see eu.scy.colemo.contributions.Contribution#getIp()
	 */
	public InetAddress getIp() {
		return ip;
	}

	/* (non-Javadoc)
	 * @see eu.scy.colemo.contributions.Contribution#getPerson()
	 */
	public Person getPerson() {
		return person;
	}
	
}
