package org.opensourcebim.ifccollection;

public class ObjectSummary {
	private String globalId;
	private String objectName;
	
	private double surfaceArea;
	private double volume;
	
	public ObjectSummary(ReStoreObject rObject) {
		this.globalId = rObject.getGlobalId();
		this.objectName = rObject.getObjectName();
		this.surfaceArea = rObject.getGeometry().getLargestFaceArea();
		this.volume = rObject.getGeometry().getVolume();
	}
	
	public String getGlobalId() {
		return globalId;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	public double getSurfaceArea() {
		return surfaceArea;
	}
	
	public double getVolume() {
		return volume;
	}
}
