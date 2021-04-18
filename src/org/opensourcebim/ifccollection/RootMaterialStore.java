package org.opensourcebim.ifccollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.BasicEList;


public class RootMaterialStore {
	
	private Map<String, RootMaterial> rootMaterials;
	
	public RootMaterialStore() {
		setRootMaterials(new HashMap<String, RootMaterial>());
	}
	
	public Map<String, RootMaterial> getRootMaterials() {
		return rootMaterials;
	}
	
	public void setRootMaterials(Map<String, RootMaterial> rootMaterials) {
		this.rootMaterials = rootMaterials;
	}
	
	public void addMaterialsByObject(ReStoreObject parentObject, List<MaterialSource> listedMaterials) {
		for(MaterialSource material : listedMaterials) {
			this.addRootMaterial(material, parentObject);
		}
	}
	
	public void addRootMaterial(MaterialSource rootMaterial, ReStoreObject parentObject) {
		if(rootMaterials.containsKey(rootMaterial.getName())) {
			RootMaterial existingMaterial = rootMaterials.get(rootMaterial.getName());
			existingMaterial.addUsedByObject(parentObject);
		} else {
			RootMaterial newMaterial = new RootMaterial(rootMaterial.getOid(), rootMaterial.getName());
			newMaterial.addUsedByObject(parentObject);
			this.getRootMaterials().putIfAbsent(newMaterial.getName(), newMaterial);
		}
		
	}
	
	public void addOrUpdateRootMaterial() {
		
	}
}
