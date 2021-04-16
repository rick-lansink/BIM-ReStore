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

import com.fasterxml.jackson.annotation.JsonIgnore;


public class ReStoreObjectImpl implements ReStoreObject {

	private long objectId;
	private String globalId;
	private String objectName;
	private List<ReStoreLayer> reStoreLayers;
	private String objectType;
	private String parentId;

	@JsonIgnore
	private Map<String, Object> properties;
	private List<MaterialSource> listedMaterials;

	private ReStoreGeometry geometry;
	private List<ReStoreInfoTag> tags;

	public ReStoreObjectImpl(long objectId, String globalId, String objectName, String objectType, String parentId) {

		this.objectId = objectId;
		this.setGlobalId(globalId);
		this.setObjectName(objectName);
		if (objectType != null) {
			objectType = objectType.replaceAll("Impl$", "");
			this.setObjectType(objectType);
		}
		this.parentId = parentId;

		initializeCollections();
	}
	
	public ReStoreObjectImpl() {
		initializeCollections();
	}
	
	private void initializeCollections() {
		reStoreLayers = new BasicEList<ReStoreLayer>();
		properties = new HashMap<String, Object>();
		tags = new ArrayList<ReStoreInfoTag>();
		this.listedMaterials = new BasicEList<MaterialSource>();
	}

	@Override
	public void addLayer(ReStoreLayer reStoreLayer) {
		reStoreLayers.add(reStoreLayer);
	}

	@Override
	public List<ReStoreLayer> getLayers() {
		return reStoreLayers;
	}

	@Override
	public long getObjectId() {
		return objectId;
	}

	@Override
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType == null ? "undefined type" : objectType;
	}

	@Override
	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName == null ? "undefined name" : objectName;
	}

	@Override
	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	@Override
	public ReStoreGeometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(ReStoreGeometry geom) {
		this.geometry = geom;
	}


	@Override
	public String getParentId() {
		return this.parentId;
	}

	@Override
	public void setParentId(String value) {
		this.parentId = value;

	}

	@JsonIgnore
	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void addProperty(String name, Object value) {
		this.properties.put(name, value);
	}

	@Override
	public List<ReStoreInfoTag> getAllTags() {
		return this.tags;
	}

	@Override
	public void clearTagsOfType(ReStoreInfoTagType type) {
		List<ReStoreInfoTag> typeTags = this.tags.parallelStream()
				.filter(t -> t.getType() == type)
				.collect(Collectors.toList());
		this.tags.removeAll(typeTags);
	}
	
	@Override
	public List<ReStoreInfoTag> getTagsByType(ReStoreInfoTagType type) {
		return tags.stream().filter(t -> t.getType().equals(type)).collect(Collectors.toList());
	}

	@Override
	public void addTag(ReStoreInfoTagType tagType, String message) {
		this.tags.add(new ReStoreInfoTag(tagType, message));
	}

	@Override
	public void addMaterialSource(String materialName, String materialGuid, String source) {
		this.getListedMaterials().add(new MaterialSource(materialGuid, materialName, source));
	}

	@Override
	public List<MaterialSource> getListedMaterials() {
		return listedMaterials;
	}

	@Override
	public List<String> getMaterialNamesBySource(String source) {
		return this.getListedMaterials().stream().filter(m -> source == null ? true : m.getSource() == source)
				.map(m -> m.getName()).collect(Collectors.toList());
	}

	@Override
	public boolean hasDuplicateMaterialNames() {
		return this.getListedMaterials().stream().distinct().collect(Collectors.toSet()).size() < this
				.getListedMaterials().size();
	}

	@Override
	public void addMaterialSource(MaterialSource source) {
		if (!source.getName().isEmpty()) {
			this.listedMaterials.add(source);
		}
	}
	
	@Override
	public String getValueHash() {
		return this.getObjectName() + this.getObjectType()
		+ String.join("-", this.getMaterialNamesBySource(null));
	}
}