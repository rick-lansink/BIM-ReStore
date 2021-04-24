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

public class RootComponentStore {

	private Map<String, RootComponent> children;
	
	public RootComponentStore() {
		setRootComponents(new HashMap<String, RootComponent>());
	}
	
	public Map<String, RootComponent> getRootComponents() {
		return children;
	}
	
	public Map<String, List<RootComponent>> getRootComponentsByType() {
		List<RootComponent> componentList = new ArrayList<RootComponent>(children.values());
		return componentList.stream().collect(Collectors.groupingBy(c -> c.getComponentType()));
	}

	
	public void setRootComponents(Map<String, RootComponent> rootComponents) {
		this.children = rootComponents;
	}
	
	public void addRootComponent(String rootComponent, List<ReStoreElement> containedObjects) {
		if(children.containsKey(rootComponent)) {
			RootComponent existingComponent = children.get(rootComponent);
			existingComponent.elementListToDimensionSet(containedObjects);
		} else {
			RootComponent newComponent = new RootComponent(rootComponent);
			String componentType = (containedObjects.get(0).getReStoreObject()).getObjectType();
			String componentName = (containedObjects.get(0).getReStoreObject()).getObjectName();
			newComponent.setComponentType(componentType);
			newComponent.setComponentName(componentName);
			newComponent.elementListToDimensionSet(containedObjects);
			this.getRootComponents().putIfAbsent(rootComponent, newComponent);
		}
	}
	
}
