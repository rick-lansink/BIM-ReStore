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

	private Map<String, RootComponent> rootComponents;
	
	public RootComponentStore() {
		setRootComponents(new HashMap<String, RootComponent>());
	}
	
	public Map<String, RootComponent> getRootComponents() {
		return rootComponents;
	}
	
	public void setRootComponents(Map<String, RootComponent> rootComponents) {
		this.rootComponents = rootComponents;
	}
	
	public void addRootComponent(String rootComponent, List<ReStoreElement> containedObjects) {
		if(rootComponents.containsKey(rootComponent)) {
			RootComponent existingComponent = rootComponents.get(rootComponent);
			existingComponent.elementListToDimensionSet(containedObjects);
		} else {
			RootComponent newComponent = new RootComponent(rootComponent);
			newComponent.elementListToDimensionSet(containedObjects);
			this.getRootComponents().putIfAbsent(rootComponent, newComponent);
		}
	}
	
}
