package org.opensourcebim.ifccollection;

public class MaterialSource {

	public MaterialSource(String oid, String name, String Source) {
		this.setOid(oid);
		this.setName(name);
		this.setSource(Source);
	}
	
	private String oid;
	private String name;
	private String source;
	
	/**
	 * the object id of the IfcMaterial object 
	 */
	public String getOid() {
		return oid;
	}
	private void setOid(String oid) {
		this.oid = oid;
	}
	
	/**
	 * the material description as in the IfcMaterial object
	 */
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 */
	public String getSource() {
		return source;
	}
	private void setSource(String source) {
		this.source = source;
	}
	
	
}