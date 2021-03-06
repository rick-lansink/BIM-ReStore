package org.opensourcebim.ifccollection;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.BasicEList;
import org.apache.commons.lang3.StringUtils;

public class RootMaterial {

	public RootMaterial(String oid, String name) {
		this.setOid(oid);
		this.setName(name);
		this.setUsedObjects(new BasicEList<ReStoreObject>());
		this.inheritedProperties = new HashMap<String, List<Object>>();
	}
	
	private String oid;
	private String name;
	private double totalSurfaceArea;
	private double totalVolume;
	private Map<String, List<Object>> inheritedProperties;
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
	
	private void setUsedObjects(List<ReStoreObject> reStoreObjects) {
		this.usedByObjects = reStoreObjects;
	}
	
	public Map<String, List<Object>> getInheritedProperties() {
		return this.inheritedProperties;
	}

	public Map<String, List<Object>> getInheristedPropertiesNoDuplicate() {
		return this.inheritedProperties.entrySet().stream()
				.filter(o -> (o.getValue().size() == 1 && o.getValue().get(0) != ""))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}
	
	public List<ReStoreObject> getUsedByObjects() {
		return usedByObjects;
	}
	
	public List<String> getUsedByObjectsGuid() {
		return usedByObjects.stream().map(o -> o.getGlobalId())
				.collect(Collectors.toList());
	}
	
	public List<ObjectSummary> getObjectSummary () {
		return usedByObjects.stream().map(o -> new ObjectSummary(o))
				.collect(Collectors.toList());
	}
	
	public void addUsedByObject(ReStoreObject object) {
		this.getUsedByObjects().add(object);
		this.addSurfaceArea(
			object.getGeometry().getLargestFaceArea()
		);
		this.addVolume(
			object.getGeometry().getVolume()
		);
		this.addPropertySetsFromObject(object);

	}
	
	public void addPropertySetsFromObject(ReStoreObject object) {
		Map<String, Object> properties = object.getProperties();
		for(Entry<String, Object> property : properties.entrySet()) {
			if (this.inheritedProperties.containsKey(property.getKey())) {
				List<Object> setProperties = this.inheritedProperties.get(property.getKey());
				if(!setProperties.contains(property.getValue())) {
					this.inheritedProperties.get(property.getKey()).add(property.getValue());
				}
			} else {
				this.inheritedProperties.putIfAbsent(property.getKey(), new ArrayList<Object>());
				this.inheritedProperties.get(property.getKey()).add(property.getValue());
			}
		}
	}
	
	
	public double getTotalSurfaceArea() {
		return totalSurfaceArea;
	}
	
	public void setTotalSurfaceArea(double surfaceArea) {
		this.totalSurfaceArea = surfaceArea;
	}
	
	public void addSurfaceArea(double surfaceArea) {
		this.totalSurfaceArea = Double.sum(this.totalSurfaceArea, surfaceArea);
	}
	
	
	public double getTotalVolume() {
		return totalVolume;
	}
	
	public void setTotalVolume(double volume) {
		this.totalVolume = volume;
	}
	
	public void addVolume(double volume) {
		this.totalVolume = Double.sum(this.totalVolume, volume);
	}
	
}
