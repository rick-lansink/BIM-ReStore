package org.opensourcebim.ifccollection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Storage container to map mpgOject to the Nmdproducts
 * 
 * @author Jasper Vijverberg
 *
 */
public class ReStoreElement {

	private String ifcName;
	private ReStoreObject mpgObject;
	private ObjectStore store;

	public ReStoreElement(String name, ObjectStore store) {
		ifcName = name;
		this.store = store;
	}

	public void setReStoreObject(ReStoreObject mpgObject) {
		this.mpgObject = mpgObject;
	}

	public ReStoreObject getReStoreObject() {
		return this.mpgObject;
	}

	@JsonIgnore
	public ObjectStore getStore() {
		return store;
	}

	/**
	 * Get the name of the material as found in the IFC file
	 * 
	 * @return
	 */
	public String getIfcName() {
		return this.ifcName;
	}

	

//	public List<Integer> getProductIds() {
//
//		return this.getReStoreObject() == null ? new ArrayList<Integer>()
//				: this.getReStoreObject().getListedMaterials().stream().filter(m -> m.getMapId() > 0).map(m -> m.getMapId())
//						.collect(Collectors.toList());
//	}


	/**
	 * determines whether an element is equal in values to another element for the
	 * sake of mapping grouping This will therefore not include an equality
	 * 
	 * @return
	 */
	public String getValueHash() {
		// TODO Auto-generated method stub
		return this.getReStoreObject().getValueHash();
	}

}