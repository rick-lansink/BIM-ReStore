package org.opensourcebim.ifccollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.BasicEList;

public class DimensionSet {
	
	public DimensionSet(String dimensionHash, List<ReStoreObject> usedByObjects) {
		this.setDimensionHash(dimensionHash);
		this.setUsedByObjects(usedByObjects);
	}

	private String dimensionSetHash;
	private double totalSurfaceArea;
	private double totalVolume;
	private List<String> children;
	
	public String getDimensionHash() {
		return dimensionSetHash;
	}
	
	private void setDimensionHash(String hash) {
		this.dimensionSetHash = hash;
	}
	
	public double getTotalSurfaceArea() {
		return totalSurfaceArea;
	}
	
	public double getTotalVolume() {
		return totalVolume;
	}
	
	public List<String> getUsedByObjects() {
		return children;
	}
	
	private void setUsedByObjects(List<ReStoreObject> usedByObjects) {
		this.children = usedByObjects.stream().map(o -> o.getGlobalId()).collect(Collectors.toList());
		this.setTotalSurfaceArea(usedByObjects);
		this.setTotalVolume(usedByObjects);
	}
	
	private void setTotalSurfaceArea(List<ReStoreObject> usedByObjects) {
		this.totalSurfaceArea = usedByObjects.stream().map(o -> o.getGeometry().getLargestFaceArea())
					.collect(Collectors.summingDouble(d -> d));
	}
	
	private void setTotalVolume(List<ReStoreObject> usedByObjects) {
		this.totalVolume = usedByObjects.stream().map(o -> o.getGeometry().getVolume())
					.collect(Collectors.summingDouble(d -> d));
	}
}
