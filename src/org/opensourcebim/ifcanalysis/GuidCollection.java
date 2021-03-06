package org.opensourcebim.ifcanalysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.opensourcebim.ifccollection.ReStoreObject;
import org.opensourcebim.ifccollection.ObjectStore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GuidCollection {

	HashSet<String> guids;
	private String description;
	List<String> materials;
	List<String> types;
	List<String> names;
	Long numberOfComponents;
	Long numberOfComposed;
	
	@JsonIgnore
	private ObjectStore store;
	
	public GuidCollection(ObjectStore store, String description) {
		guids = new HashSet<String>();
		this.store = store;
		this.setDescription(description);
	}
	
	public void setCollection(List<String> guids){
		this.guids = new HashSet<String>(guids);
		update();
	}
		
	public int getSize() {
		return guids.size();
	}
	
	public Set<String> getGuids()
	{
		return this.guids;
	}
	
	public List<String> getTypes() {
		return this.types;
	}
	
	public List<String> getNames() {
		return this.names;
	}
	
	public List<String> getMaterials() {
		return this.materials;
	}
	
	public Long getNumberOfComponents() {
		return this.numberOfComponents;
	}
	
	public Long getNumberOfComposed() {
		return this.numberOfComposed;
	}
	
	public String getDescription() {
		return description;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}	

	public void reset() {
		this.guids.clear();
		update();
	}
	
	private void update() {
		List<ReStoreObject> selectedObjects = store.getObjectsByGuids(guids);
		types = selectedObjects.stream().map(o -> o.getObjectType()).distinct()
				.collect(Collectors.toList());
		materials = selectedObjects.stream().flatMap(o -> o.getMaterialNamesBySource(null).stream()).distinct()
				.collect(Collectors.toList());
		names = selectedObjects.stream().map(o -> o.getObjectName()).distinct()
				.collect(Collectors.toList());
		numberOfComposed = selectedObjects.stream().filter(o -> store.getChildren(o.getGlobalId()).count() > 0).count();
		numberOfComponents = selectedObjects.stream().filter(o -> o.getParentId() != null && o.getParentId() != "").count();
	}
}