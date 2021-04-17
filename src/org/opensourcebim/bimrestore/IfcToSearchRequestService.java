package org.opensourcebim.bimrestore;

import org.bimserver.bimbots.BimBotContext;
import org.bimserver.bimbots.BimBotsException;
import org.bimserver.bimbots.BimBotsInput;
import org.bimserver.bimbots.BimBotsOutput;
import org.bimserver.plugins.PluginConfiguration;
import org.opensourcebim.ifcanalysis.GuidDataSet;
import org.opensourcebim.ifcanalysis.GuidDataSetGroupedElements;
import org.opensourcebim.ifcanalysis.GuidDataSetRootMaterials;
import org.opensourcebim.ifccollection.ReStoreIfcObjectCollector;


public class IfcToSearchRequestService extends IfcObjectCollectionBaseService {
	
	@Override
	public BimBotsOutput runBimBot(BimBotsInput input, BimBotContext bimBotContext, PluginConfiguration pluginConfiguration)
			throws BimBotsException {

		// Get properties from ifcModel
		ReStoreIfcObjectCollector matParser = new ReStoreIfcObjectCollector();
		this.setStore(matParser.collectIfcModelObjects(input, bimBotContext.getContextId()));
		
		//GuidDataSet dataset = new GuidDataSet(this.getStore());
		//GuidDataSetGroupedElements dataset = new GuidDataSetGroupedElements(this.getStore());
		GuidDataSetRootMaterials dataset = new GuidDataSetRootMaterials(this.getStore());
		return this.toBimBotsJsonOutput(dataset, "guid property dataset results");
	}

	@Override
	public String getOutputSchema() {
		return "GUID_PROPERTIES_DATASET_0_0_1";
	}
	
}
