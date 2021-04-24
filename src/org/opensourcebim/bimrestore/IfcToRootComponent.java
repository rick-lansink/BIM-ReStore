package org.opensourcebim.bimrestore;

import org.bimserver.bimbots.BimBotContext;
import org.bimserver.bimbots.BimBotsException;
import org.bimserver.bimbots.BimBotsInput;
import org.bimserver.bimbots.BimBotsOutput;
import org.bimserver.plugins.PluginConfiguration;
import org.opensourcebim.ifcanalysis.GuidDataSet;
import org.opensourcebim.ifcanalysis.GuidDataSetGroupedElements;
import org.opensourcebim.ifcanalysis.GuidDataSetRootMaterials;
import org.opensourcebim.ifcanalysis.GuidDataSetRootComponents;
import org.opensourcebim.ifccollection.ReStoreIfcObjectCollector;


public class IfcToRootComponent extends IfcObjectCollectionBaseService {
	
	@Override
	public BimBotsOutput runBimBot(BimBotsInput input, BimBotContext bimBotContext, PluginConfiguration pluginConfiguration)
			throws BimBotsException {

		// Get properties from ifcModel
		ReStoreIfcObjectCollector matParser = new ReStoreIfcObjectCollector();
		this.setStore(matParser.collectIfcModelObjects(input, bimBotContext.getContextId()));
		
		GuidDataSetRootComponents dataset = new GuidDataSetRootComponents(this.getStore());
		return this.toBimBotsJsonOutput(dataset, "root component dataset results");
	}

	@Override
	public String getOutputSchema() {
		return "ROOT_COMPONENTS_DATASET_0_0_1";
	}
	
}
