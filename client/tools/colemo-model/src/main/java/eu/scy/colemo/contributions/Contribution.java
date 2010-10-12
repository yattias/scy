/*
 * Created on 13.nov.2004
 *
 * 
 */
package eu.scy.colemo.contributions;

import java.net.InetAddress;

import eu.scy.colemo.network.Person;

/**
 *
 *
 */
public interface Contribution {
	public InetAddress getIp();
	public Person getPerson();
}