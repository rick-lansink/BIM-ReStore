package org.opensourcebim.ifccollection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface ReStoreObject {
	
	long getObjectId();
	String getObjectName();
	String getObjectType();
	String getGlobalId();
	String getParentId();
	void setParentId(String value);
	
	ReStoreGeometry getGeometry();
	
	List<ReStoreLayer> getLayers();
	void addMaterialSource(String name, String guid, String source);
	void addMaterialSource(MaterialSource source);
	List<MaterialSource> getListedMaterials();
	List<String> getMaterialNamesBySource(String source);

	Map<String, Object> getProperties();
	
	List<ReStoreInfoTag> getAllTags();
	List<ReStoreInfoTag> getTagsByType(ReStoreInfoTagType type);
	
	void addLayer(ReStoreLayer layer);
	
	boolean hasDuplicateMaterialNames();
	
	void addTag(ReStoreInfoTagType tagType, String message);
	void clearTagsOfType(ReStoreInfoTagType nmdproductcardwarning);
	String getValueHash();

}