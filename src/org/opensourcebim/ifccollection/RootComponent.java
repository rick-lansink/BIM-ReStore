package org.opensourcebim.ifccollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.BasicEList;

public class RootComponent {
	
	public RootComponent(String valueHash) {
		this.setValueHash(valueHash);
		this.setDimensionSets(new BasicEList<DimensionSet>());
		this.inheritedProperties = new HashMap<String, List<Object>>();
	}

	private String valueHash;
	private String type;
	private String name;
	private double totalSurfaceArea;
	private double totalVolume;
	private Map<String, List<Object>> inheritedProperties;
	private List<DimensionSet> children;
	
	public String getValueHash() {
		return valueHash;
	}
	
	private void setValueHash(String valueHash) {
		this.valueHash = valueHash;
	}
	
	public String getComponentType() {
		return type;
	}
	
	public String getComponentName() {
		return name;
	}
	
	public double getTotalSurfaceArea() {
		return totalSurfaceArea;
	}
	
	public double getTotalVolume() {
		return totalVolume;
	}
	
	public Map<String, List<Object>> getInheritedProperties() {
		return this.inheritedProperties;
	}
	
	public void elementListToDimensionSet(List<ReStoreElement> elements) {
		List<ReStoreObject> convertedObjects = RootComponent.elementListToObjectList(elements);
		this.addPropertySetsFromObjects(convertedObjects);
		Map<String, List<ReStoreObject>> groupedObjects = RootComponent.objectsGroupedByDimensions(convertedObjects);
		
		for(Entry<String, List<ReStoreObject>> dimensionGroup : groupedObjects.entrySet()) {
			DimensionSet dimensionSet = new DimensionSet(
				dimensionGroup.getKey(),
				dimensionGroup.getValue()
			);
			this.addDimensionSet(dimensionSet);
		}
		
	}
	
	public void addPropertySetsFromObjects(List<ReStoreObject> objects) {
		for(ReStoreObject object : objects) {
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
		this.inheritedProperties = this.getInheristedPropertiesNoDuplicate();
	}
	
	public Map<String, List<Object>> getInheristedPropertiesNoDuplicate() {
		return this.inheritedProperties.entrySet().stream()
				.filter(o -> (o.getValue().size() == 1 && o.getValue().get(0) != ""))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}
	
	public List<DimensionSet> getDimensionSets() {
		return this.children;
	}
	
	public List<String> getDimensionSetsStringList() {
		return this.children.stream().map(d -> d.getDimensionHash()).collect(Collectors.toList());
	}
	
	public void setDimensionSets(List<DimensionSet> dSets) {
		this.children = dSets;
	}
	
	public void setComponentType(String type) {
		this.type = type;
	}
	
	public void setComponentName(String name) {
		this.name = name;
	}
	
	
	public void addDimensionSet(DimensionSet dSet) {
		this.children.add(dSet);
	}
	
	public static List<ReStoreObject> elementListToObjectList(List<ReStoreElement> elements) {
		return elements.stream()
				.map(e -> e.getReStoreObject())
				.collect(Collectors.toList());
	}
	
	public static Map<String, List<ReStoreObject>> objectsGroupedByDimensions(List<ReStoreObject> reStoreObjects) {
		return reStoreObjects.stream().collect(Collectors.groupingBy(o -> {
			return Arrays.toString((o.getGeometry()).getSortedDimensions());
		}));
	}
	

//	private void setUsedByObjects(List<ReStoreObject> reStoreObjects) {
//		this.usedByObjects = reStoreObjects;
//	}
//	
//	public void addUsedByObjects(List<ReStoreElement> elements) {
//		List<ReStoreObject> convertedObjects = elements.stream()
//				.map(e -> e.getReStoreObject())
//				.collect(Collectors.toList());
//		this.getUsedByObjects().addAll(convertedObjects);
//	}
//	
//	public List<ReStoreObject> getUsedByObjects() {
//		return usedByObjects;
//	}
//	
//	public List<String> getUsedByObjectsGuid() {
//		return usedByObjects.stream().map(o -> o.getGlobalId())
//				.collect(Collectors.toList());
//	}
	
}
