package org.opensourcebim.ifccollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.bimserver.utils.AreaUnit;
import org.bimserver.utils.LengthUnit;
import org.bimserver.utils.VolumeUnit;
import org.opensourcebim.ifcanalysis.GuidCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface ObjectStore {

	void reset();
	
	HashSet<ReStoreElement> getElements();
	
	@JsonIgnore
	List<ReStoreObject> getObjects();
	List<ReStoreSpace> getSpaces();
	
	Stream<String> getAllMaterialNames();
	
	void setObjectForElement(String name, ReStoreObject reStoreObject);
	void addObject(ReStoreObject reStoreObject);
	List<ReStoreObject> getObjectsByGuids(HashSet<String> guids);
	Optional<ReStoreObject> getObjectByGuid(String guid);
	Stream<ReStoreObject> getChildren(String parentGuid);
	
	LengthUnit getLengthUnit();
	AreaUnit getAreaUnit();
	VolumeUnit getVolumeUnit();
	
	ReStoreElement addElement(String string);
	ReStoreElement getElementByName(String name);
	List<ReStoreElement> getElementsByProductType(String productType);
	ReStoreElement getElementByObjectGuid(String guid);
	Map<String, List<ReStoreElement>> getElementGroups();
	Map<String, List<ReStoreElement>> getCleanedElementGroups();

	
	void addSpace(ReStoreSpace space);
	double getTotalVolumeOfMaterial(String name);
	double getTotalFloorArea();
	
	boolean isIfcDataComplete();
	
	@JsonIgnore
	GuidCollection getGuidsWithoutMaterial();
	@JsonIgnore
	GuidCollection getGuidsWithoutMaterialAndWithoutFullDecomposedMaterials();
	@JsonIgnore
	GuidCollection getGuidsWithoutVolume();
	@JsonIgnore
	GuidCollection getGuidsWithoutVolumeAndWithoutFullDecomposedVolumes();
	@JsonIgnore
	GuidCollection getGuidsWithRedundantMaterials();
	@JsonIgnore
	GuidCollection getGuidsWithUndefinedLayerMats();
	
	boolean hasUndefinedVolume(ReStoreObject obj, boolean includeChildren);

	boolean hasRedundantMaterials(ReStoreObject obj, boolean includeChildren);

	boolean hasUndefinedLayers(ReStoreObject obj, boolean includeChildren);

	boolean hasUndefinedMaterials(ReStoreObject obj, boolean includeChildren);

	String getProjectId();

	List<Double[]> getDimensionsOfElementGroup(List<ReStoreElement> elementList);
}