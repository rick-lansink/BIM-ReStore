package org.opensourcebim.ifcanalysis;

import java.util.HashMap;

public class GuidPropertyRecord {
		
	private HashMap<String, Object> children;
	
	public GuidPropertyRecord() {
		children = new HashMap<String, Object> ();
	}
	
	public HashMap<String, Object> getValues() {
		return children;
	}

	public void addOrSetColumn(String title, Object value) {
		children.put(title, value);
	}
}