package org.opensourcebim.ifccollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RootMaterial {

	public RootMaterial(String oid, String name) {
		this.setOid(oid);
		this.setName(name);
	}
	
	private String oid;
	private String name;
	private double totalSurfaceArea;
	private double totalVolume;
	
	private List<ReStoreObject> usedByObjects;
	
	
	public String getOid() {
		return oid;
	}
	
	private void setOid(String oid) {
		this.oid = oid;
	}
	
	
	
	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	
	public List<ReStoreObject> getUsedByObjects() {
		return usedByObjects;
	}
	
	public List<String> getUsedByObjectsGuid() {
		return usedByObjects.stream().map(o -> o.getGlobalId())
				.collect(Collectors.toList());
	}
	
	public void addUsedByObject(ReStoreObject object) {
		this.usedByObjects.add(object);
	}
	
	
	public double getTotalSurfaceArea() {
		return totalSurfaceArea;
	}
	
	public void setTotalSurfaceArea(double surfaceArea) {
		this.totalSurfaceArea = surfaceArea;
	}
	
	public void addSurfaceArea(double surfaceArea) {
		this.totalSurfaceArea += surfaceArea;
	}
	
	
	public double getTotalVolume() {
		return totalVolume;
	}
	
	public void setTotalVolume(double volume) {
		this.totalVolume = volume;
	}
	
	public void addVolume(double volume) {
		this.totalVolume += volume;
	}
	
}
