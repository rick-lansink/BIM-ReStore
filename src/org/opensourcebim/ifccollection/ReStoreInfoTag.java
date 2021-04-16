
package org.opensourcebim.ifccollection;

public class ReStoreInfoTag {
	private ReStoreInfoTagType type;
	private String message;
	
	public ReStoreInfoTag(ReStoreInfoTagType type, String message) {
		this.type = type;
		this.message = message;
	}

	public ReStoreInfoTagType getType() {
		return this.type;
	}
	
	public String getMessage() {
		return this.message;
	}
}