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
	
	public RootComponent(String valueHash, List<ReStoreElement> containedElements) {
		this.setValueHash(valueHash);
		//this.setUsedByObjects(new BasicEList<ReStoreObject>());
	}

	private String valueHash;
	private double totalSurfaceArea;
	private double totalVolume;
	
	private Map<String, List<DimensionSet>> dimensionSets;
	
	public String getValueHash() {
		return valueHash;
	}
	
	private void setValueHash(String valueHash) {
		this.valueHash = valueHash;
	}
	
	public double getTotalSurfaceArea() {
		return totalSurfaceArea;
	}
	
	public double getTotalVolume() {
		return totalVolume;
	}
	
	public void elementListToDimensionSet(List<ReStoreElement> elements) {
		List<ReStoreObject> convertedObjects = RootComponent.elementListToObjectList(elements);
		Map<String, List<ReStoreObject>> groupedObjects = RootComponent.objectsGroupedByDimensions(convertedObjects);
		
		for(Entry<String, List<ReStoreObject>> dimensionGroup : groupedObjects.entrySet()) {
			DimensionSet dimensionSet = new DimensionSet(
				dimensionGroup.getKey(),
				dimensionGroup.getValue()
			);
			//this.addDimensionSet(dimensionSet.getDimensionHash(), dimensionSet);
		}
		
	}
	
//	public void addDimensionSet(String dimensionHash, DimensionSet dSet) {
//		this.dimensionSets.put(valueHash, null)
//		
//	}
	
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
