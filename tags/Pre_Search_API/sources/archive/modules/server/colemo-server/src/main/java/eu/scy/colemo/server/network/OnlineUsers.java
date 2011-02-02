/*
 * Created on 27.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.network;

import eu.scy.colemo.network.Person;

import java.io.Serializable;


/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OnlineUsers implements Serializable{
	
	private Person[] users= new Person[10];
	private int size=0;
	public OnlineUsers(){
		
	}
	public OnlineUsers(Person[] users,int size){
		this.users=users;
		this.size=size;
	}
	public void addUser(Person p){
		users[size]=p;
		size++;
	}
	public int getSize() {
		return size;
	}
	public Person personAt(int i) {
		return users[i];
	}
	public Person[] getAll() {
		return users;
	} 
	public void removePerson(Person p){
		boolean found=false;
		int i=0;
		while(!found){
			if(p.equals(personAt(i))){
			}
			else i++;
		}
	}
	
}
