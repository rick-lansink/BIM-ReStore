package org.opensourcebim.ifccollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bimserver.utils.AreaUnit;
import org.bimserver.utils.LengthUnit;
import org.bimserver.utils.VolumeUnit;
import org.eclipse.emf.common.util.BasicEList;
import org.opensourcebim.ifcanalysis.GuidCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Storage container for collected ifc objects. Only data relevant for Mpg
 * calculations should be stored here
 * 
 * @author vijj
 *
 */
public class ObjectStoreImpl implements ObjectStore {

	private HashSet<ReStoreElement> reStoreElements;

	@JsonIgnore
	private List<ReStoreObject> reStoreObjects;

	private List<ReStoreSpace> spaces;
	
	private String projectId;

	/**
	 * list to store the guids with any MpgObjects that the object linked to that
	 * guid decomposes
	 */
	@JsonIgnore
	private List<ImmutablePair<String, ReStoreObject>> decomposedRelations;

	private VolumeUnit volumeUnit;
	private AreaUnit areaUnit;
	private LengthUnit lengthUnit;

	public ObjectStoreImpl() {
		setElements(new HashSet<>());
		setObjects(new BasicEList<ReStoreObject>());
		setSpaces(new BasicEList<ReStoreSpace>());
		setUnits(VolumeUnit.CUBIC_METER, AreaUnit.SQUARED_METER, LengthUnit.METER);
		decomposedRelations = new ArrayList<ImmutablePair<String, ReStoreObject>>();
	}

	public void reset() {
		decomposedRelations.clear();
		reStoreObjects.clear();
		reStoreElements.clear();
		spaces.clear();
	}

	@Override
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public VolumeUnit getVolumeUnit() {
		return this.volumeUnit;
	}

	@Override
	public AreaUnit getAreaUnit() {
		return this.areaUnit;
	}

	@Override
	public LengthUnit getLengthUnit() {
		return this.lengthUnit;
	}

	/**
	 * Set the units used when storing the mpgObjects
	 * 
	 * @param volumeUnit volume unit fo ifcProduct
	 * @param areaUnit   area unit of ifcProducts
	 * @param lengthUnit length unit of ifcProducts
	 */
	public void setUnits(VolumeUnit volumeUnit, AreaUnit areaUnit, LengthUnit lengthUnit) {
		this.volumeUnit = volumeUnit;
		this.areaUnit = areaUnit;
		this.lengthUnit = lengthUnit;
	}

	@Override
	public ReStoreElement addElement(String name) {
		ReStoreElement el = null;
		if (name != null && !name.isEmpty()) {
			el = new ReStoreElement(name, this);
			reStoreElements.add(el);
		}
		return el;
	}

	@Override
	public HashSet<ReStoreElement> getElements() {
		return reStoreElements;
	}

	private void setElements(HashSet<ReStoreElement> reStoreElements) {
		this.reStoreElements = reStoreElements;
	}

	@Override
	public void setObjectForElement(String name, ReStoreObject reStoreObject) {
		ReStoreElement el = getElementByName(name);
		if (el != null) {
			el.setReStoreObject(reStoreObject);
		}
	}

	@Override
	public void addObject(ReStoreObject reStoreObject) {
		this.getObjects().add(reStoreObject);
	}
	
	@Override
	public List<ReStoreObject> getObjects() {
		return reStoreObjects;
	}

	private void setObjects(List<ReStoreObject> reStoreObjects) {
		this.reStoreObjects = reStoreObjects;
	}

	@Override
	public List<ReStoreSpace> getSpaces() {
		return spaces;
	}

	private void setSpaces(List<ReStoreSpace> spaces) {
		this.spaces = spaces;
	}
	

	private List<ReStoreObject> getObjectsByProductType(String productType) {
		return reStoreObjects.stream().filter(g -> g.getObjectType().equals(productType)).collect(Collectors.toList());
	}

	private List<ReStoreSpace> getObjectsByMaterialName(String materialName) {
		return reStoreObjects.stream().flatMap(g -> g.getLayers().stream()).filter(o -> o.getMaterialName() != null)
				.filter(o -> o.getMaterialName().equals(materialName)).collect(Collectors.toList());
	}

	@Override
	public Optional<ReStoreObject> getObjectByGuid(String guidId) {
		return reStoreObjects.stream().filter(o -> guidId.equals(o.getGlobalId())).findFirst();
	}

	@Override
	public List<ReStoreObject> getObjectsByGuids(HashSet<String> guidIds) {
		return reStoreObjects.stream().filter(o -> guidIds.contains(o.getGlobalId())).collect(Collectors.toList());
	}

	@Override
	public ReStoreElement getElementByName(String name) {
		Optional<ReStoreElement> element = getElements().stream().filter(e -> e.getIfcName().equalsIgnoreCase(name))
				.findFirst();
		return element.isPresent() ? element.get() : null;
	}

	@Override
	public List<ReStoreElement> getElementsByProductType(String productType) {
		List<ReStoreObject> objectsByProductType = this.getObjectsByProductType(productType);
		List<String> materialNames = objectsByProductType.stream()
				.flatMap(o -> o.getMaterialNamesBySource(null).stream()).distinct().collect(Collectors.toList());

		return materialNames.stream().map(mat -> this.getElementByName(mat)).collect(Collectors.toList());
	}

	@Override
	public ReStoreElement getElementByObjectGuid(String guid) {
		Optional<ReStoreElement> el = this.getElements().stream().filter(e -> e.getReStoreObject().getGlobalId().equals(guid))
				.findFirst();
		if (el.isPresent()) {
			return el.get();
		} else {
			return null;
		}
	}
	
	/**
	 * Group the elements by an equalByValues
	 */
	@JsonIgnore
	@Override
	public Map<String, List<ReStoreElement>> getElementGroups() {
		return this.reStoreElements.stream().collect(Collectors.groupingBy(el -> {
			return el.getValueHash();
		}));
	}
	
	@JsonIgnore
	@Override
	public Map<String, List<ReStoreElement>> getCleanedElementGroups() {
		return this.reStoreElements.stream().filter(el -> !el.getReStoreObject().getObjectName().isEmpty()).collect(Collectors.groupingBy(el -> {
			return el.getValueHash();
		}));
	}

	@Override
	public double getTotalVolumeOfMaterial(String name) {
		return getObjectsByMaterialName(name).stream()
				.collect(Collectors.summingDouble(o -> o == null ? 0.0 : o.getVolume()));
	}

	@Override
	public List<Double[]> getDimensionsOfElementGroup(List<ReStoreElement> elementList) {
		return elementList.stream()
				.map(e -> ((e.getReStoreObject()).getGeometry()).getSortedDimensions())
				.distinct()
				.collect(Collectors.toList());
	}
	
	@Override
	public void addSpace(ReStoreSpace space) {
		spaces.add(space);
	}

	@Override
	public double getTotalFloorArea() {
		return spaces.stream().map(s -> s.getArea()).collect(Collectors.summingDouble(a -> a == null ? 0.0 : a));
	}

	/**
	 * Create links from and to Decomposing and Decomposed products based on a guid
	 * map
	 * 
	 * @param isDecomposedByrelationMap hashMap containing the guids of decomposed
	 *                                  and decomposing objects.
	 */
	public void reloadParentChildRelationShips(Map<String, String> isDecomposedByrelationMap) {

		this.getObjects().forEach(o -> {
			if (isDecomposedByrelationMap.containsKey(o.getGlobalId())) {
				o.setParentId(isDecomposedByrelationMap.get(o.getGlobalId()));
			}
		});

		// create a list with parent ids linked to child objects
		this.decomposedRelations = this.getObjects().stream().filter(o -> !StringUtils.isBlank(o.getParentId()))
				.map(o -> new ImmutablePair<String, ReStoreObject>(o.getParentId(), o)).collect(Collectors.toList());
	}

	@Override
	public Stream<ReStoreObject> getChildren(String parentGuid) {
		if (this.decomposedRelations != null) {
			return this.decomposedRelations.parallelStream().filter(p -> p.getLeft() == parentGuid)
					.map(p -> p.getRight());
		} else {
			return (new ArrayList<ReStoreObject>()).stream();
		}
	}


	@Override
	public boolean isIfcDataComplete() {
		return getGuidsWithoutMaterial().getSize() == 0 && getGuidsWithoutVolume().getSize() == 0
				&& getGuidsWithRedundantMaterials().getSize() == 0 && getGuidsWithUndefinedLayerMats().getSize() == 0;
	}

	
	
	/**
	 * get a collection of MpgElements that are higher up in the hierarchy than the
	 * input element guid
	 * 
	 * @param globalId guid to start search
	 * @return collection of elements that have the input guid as a (recursive)
	 *         child
	 */
	private List<ReStoreElement> allParentElementsByGuid(String globalId) {
		List<ReStoreElement> elements = new ArrayList<ReStoreElement>();
		Optional<String> parentId = this.decomposedRelations.stream()
				.filter(v -> v.getValue().getGlobalId().equals(globalId)).map(v -> v.getKey()).findFirst();
		if (parentId.isPresent() && !parentId.get().isEmpty()) {
			ReStoreElement parent = this.getElementByObjectGuid(parentId.get());
			elements.add(parent);
			elements.addAll(allParentElementsByGuid(parent.getReStoreObject().getGlobalId()));
		}
		return elements;
	}

	/**
	 * Get a collection of elements that are down in the hierarchy than the input
	 * guid
	 * 
	 * @param globalId guid to start from
	 * @return a list of elements that are a (recursive) child of the input guid
	 */
	private List<ReStoreElement> allChildElementsByGuid(String globalId) {
		List<ReStoreElement> elements = this.reStoreElements.stream()
				.filter(el -> el.getReStoreObject().getParentId().equals(globalId)).collect(Collectors.toList());
		List<ReStoreElement> childEl = new ArrayList<ReStoreElement>();
		elements.forEach(el -> {
			childEl.addAll(allChildElementsByGuid(el.getReStoreObject().getGlobalId()));
		});
		elements.addAll(childEl);
		return elements;
	}

	@Override
	@JsonIgnore
	public Stream<String> getAllMaterialNames() {
		return this.getElements().stream().flatMap(e -> e.getReStoreObject().getListedMaterials().stream())
				.map(s -> s.getName()).filter(n -> {
					return (n != null && !n.isEmpty());
				}).distinct();
	}
	
	@Override
	public Map<String, RootMaterial> getAllRootMaterials() {
		List<RootMaterial> rootMaterials = new ArrayList<RootMaterial>();
		RootMaterialStore materialStore = new RootMaterialStore();
		for(ReStoreObject object : this.getObjects()) {
			materialStore.addMaterialsByObject(
					object, object.getListedMaterials()
			);
		}
		return materialStore.getRootMaterials();
	}
	
	@Override
	public Map<String, RootComponent> getAllRootComponents() {
		List<RootComponent> rootComponents = new ArrayList<RootComponent>();
		RootComponentStore componentStore = new RootComponentStore();
		for(Entry<String, List<ReStoreElement>> objectGroup : this.getCleanedElementGroups().entrySet()) {
			componentStore.addRootComponent(objectGroup.getKey(), objectGroup.getValue());
		}
		return componentStore.getRootComponents();
	}

	@Override
	@JsonIgnore
	public GuidCollection getGuidsWithoutMaterial() {
		GuidCollection coll = new GuidCollection(this, "Object GUIDs that have missing materials");
		coll.setCollection(reStoreObjects.stream().filter(o -> hasUndefinedMaterials(o, false)).map(o -> o.getGlobalId())
				.collect(Collectors.toList()));
		return coll;
	}

	@Override
	@JsonIgnore
	public GuidCollection getGuidsWithoutMaterialAndWithoutFullDecomposedMaterials() {

		GuidCollection coll = new GuidCollection(this,
				"Object GUIDs without material and any of the decomposed objects without material");
		coll.setCollection(reStoreObjects.stream().filter(o -> hasUndefinedMaterials(o, true)).map(o -> o.getGlobalId())
				.collect(Collectors.toList()));
		return coll;
	}

	@Override
	@JsonIgnore
	public GuidCollection getGuidsWithoutVolume() {
		GuidCollection coll = new GuidCollection(this, "Object GUIDs that have missing volumes");
		coll.setCollection(reStoreObjects.stream().filter(o -> hasUndefinedVolume(o, false)).map(o -> o.getGlobalId())
				.collect(Collectors.toList()));
		return coll;
	}

	@Override
	@JsonIgnore
	public GuidCollection getGuidsWithoutVolumeAndWithoutFullDecomposedVolumes() {
		GuidCollection coll = new GuidCollection(this,
				"Object GUIDs without volume and any of the decomposed objects without volume");
		coll.setCollection(reStoreObjects.stream().filter(o -> hasUndefinedVolume(o, true)).map(o -> o.getGlobalId())
				.collect(Collectors.toList()));
		return coll;
	}

	@Override
	@JsonIgnore
	public GuidCollection getGuidsWithRedundantMaterials() {
		GuidCollection coll = new GuidCollection(this, "Object GUIDs that cannot be linked to materials 1-on-1");
		coll.setCollection(reStoreObjects.stream().filter(o -> hasRedundantMaterials(o, false)).map(o -> o.getGlobalId())
				.collect(Collectors.toList()));
		return coll;
	}

	@Override
	@JsonIgnore
	public GuidCollection getGuidsWithUndefinedLayerMats() {
		GuidCollection coll = new GuidCollection(this, "Object GUIDsthat have undefined layers");
		coll.setCollection(reStoreObjects.stream().filter(o -> hasUndefinedLayers(o, false)).map(g -> g.getGlobalId())
				.collect(Collectors.toList()));
		return coll;
	}
	
	
	/**
	 * Recursive check method to validate whether a material or any of its children
	 * have undefined materials
	 */
	@Override
	public boolean hasUndefinedMaterials(ReStoreObject obj, boolean includeChildren) {
		long numLayers = obj.getLayers().size();
		boolean objIsUndefined = (numLayers + obj.getMaterialNamesBySource(null).size()) == 0;

		// anyMatch returns false on an empty list, so if children should be included,
		// but no children are present it will still return false
		boolean hasChildren = getChildren(obj.getGlobalId()).count() > 0;
		boolean childrenAreUndefined = includeChildren
				&& getChildren(obj.getGlobalId()).anyMatch(o -> hasUndefinedMaterials(o, includeChildren));

		return objIsUndefined && !hasChildren ? objIsUndefined : childrenAreUndefined;
	}

	@Override
	public boolean hasUndefinedVolume(ReStoreObject obj, boolean includeChildren) {
		boolean ownCheck = obj.getGeometry() == null || obj.getGeometry().getVolume() == 0;
		boolean hasChildren = getChildren(obj.getGlobalId()).count() > 0;
		boolean childCheck = includeChildren
				&& getChildren(obj.getGlobalId()).anyMatch(o -> hasUndefinedVolume(o, includeChildren));

		return ownCheck && !hasChildren ? true : childCheck;
	}

	@Override
	public boolean hasRedundantMaterials(ReStoreObject obj, boolean includeChildren) {
		long numLayers = obj.getLayers().size();
		boolean ownCheck = (numLayers == 0) && (obj.getMaterialNamesBySource(null).size() > 1)
				|| obj.hasDuplicateMaterialNames();
		boolean childCheck = includeChildren
				&& getChildren(obj.getGlobalId()).anyMatch(o -> hasRedundantMaterials(o, includeChildren));
		return ownCheck || childCheck;
	}

	@Override
	public boolean hasUndefinedLayers(ReStoreObject obj, boolean includeChildren) {
		long numLayers = obj.getLayers().size();
		long unresolvedLayers = obj.getLayers().stream()
				.filter(l -> l.getMaterialName() == "" || l.getMaterialName() == null).collect(Collectors.toList())
				.size();

		boolean ownCheck = (numLayers > 0) && (unresolvedLayers > 0);
		boolean childCheck = includeChildren
				&& getChildren(obj.getGlobalId()).anyMatch(o -> hasUndefinedLayers(o, includeChildren));
		return ownCheck || childCheck;
	}
}