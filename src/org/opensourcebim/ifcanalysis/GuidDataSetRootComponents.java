package org.opensourcebim.ifcanalysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.opensourcebim.ifccollection.ReStoreObject;
import org.opensourcebim.ifccollection.ReStoreElement;
import org.opensourcebim.ifccollection.RootComponent;
import org.opensourcebim.ifccollection.ObjectStore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GuidDataSetRootComponents {
	
	@JsonIgnore
	private HashSet<String> columnDefinitions;
	
	private HashMap<String, GuidPropertyRecord> records;
		
	public GuidDataSetRootComponents(ObjectStore store) {
		
		records = new HashMap<String, GuidPropertyRecord>();
		columnDefinitions = new HashSet<String>();
		
		for (Entry<String, RootComponent> map : (store.getAllRootComponents()).entrySet()) {
			RootComponent component = map.getValue();
			String key = map.getKey();

			if(StringUtils.isBlank(key)) {
				continue;
			}

			this.addRecord(key);
			this.setRecordValue(key, "componentName", key);
			this.setRecordValue(key, "dimensionSets", component.getDimensionSetsStringList());
//			this.setRecordValue(key, "oid", component.getOid());
//			this.setRecordValue(key, "volume", component.getTotalVolume());
//			this.setRecordValue(key, "surfaceArea", component.getTotalSurfaceArea());
//			this.setRecordValue(key, "dimensionSets", component.getUsedByObjects().size());
//			this.setRecordValue(key,  "usedBy", component.getObjectSummary());

		}

	}
	
	public HashMap<String, GuidPropertyRecord> getRecords() {
		return records;
	}
	
	public GuidPropertyRecord getRecordByGuid(String guid) {
		return this.records.getOrDefault(guid, null);
	}
	
	
	public void addRecord(String guid) {
		this.records.putIfAbsent(guid, createNewRecord());
	}
	
	private GuidPropertyRecord createNewRecord() {
		GuidPropertyRecord rec = new GuidPropertyRecord();
		for (String colTitle : columnDefinitions) {
			rec.addOrSetColumn(colTitle, null);
		}
		return rec;
	}
	
	
	public void setRecordValue(String guid, String columnTitle, Object value) {
		
		if (!columnDefinitions.contains(columnTitle)) {
			addColumn(columnTitle);
		}
		
		this.records.get(guid).addOrSetColumn(columnTitle, value);
	}
	
	@JsonIgnore
	public HashSet<String> getColumnDefinitions() {
		return columnDefinitions;
	}
	
	public void addColumn(String title) {
		this.columnDefinitions.add(title);
		// add the new column for all records that already exist
		for (GuidPropertyRecord record : records.values()) {
			record.addOrSetColumn(title, null);
		}
	}
	
	public void reset() {
		getRecords().clear();
		getColumnDefinitions().clear();
	}	
}