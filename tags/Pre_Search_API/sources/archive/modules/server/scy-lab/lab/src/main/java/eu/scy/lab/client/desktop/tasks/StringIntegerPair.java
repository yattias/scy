package eu.scy.lab.client.desktop.tasks;

import java.util.Map.Entry;

public class StringIntegerPair<Integer,String> implements Entry{

		private Integer key;
		private String value;
		
		public StringIntegerPair (Integer key, String value){
			this.key = key;
			this.value=value;
		}
		
		public Object getKey() {
			// TODO Auto-generated method stub
			return this.key;
		}

		public Object getValue() {
			// TODO Auto-generated method stub
			return this.value;
		}

		public Object setValue(Object value) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

