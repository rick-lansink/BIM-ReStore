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
	
	private List<String> children;
	
	public String getDimensionHash() {
		return dimensionSetHash;
	}
	
	private void setDimensionHash(String hash) {
		this.dimensionSetHash = hash;
	}
	
	public List<String> getUsedByObjects() {
		return children;
	}
	
	private void setUsedByObjects(List<ReStoreObject> usedByObjects) {
		this.children = usedByObjects.stream().map(o -> o.getGlobalId()).collect(Collectors.toList());
	}
}
