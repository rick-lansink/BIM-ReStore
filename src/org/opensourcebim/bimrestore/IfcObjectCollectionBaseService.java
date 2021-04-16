package org.opensourcebim.bimrestore;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bimserver.bimbots.BimBotsException;
import org.bimserver.bimbots.BimBotsOutput;
import org.bimserver.plugins.services.BimBotAbstractService;
import org.opensourcebim.ifccollection.ObjectStore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class IfcObjectCollectionBaseService extends BimBotAbstractService {

	private ObjectStore store = null;

	@Override
	public boolean preloadCompleteModel() {
		return true;
	}

	@Override
	public boolean requiresGeometry() {
		return true;
	}

	@Override
	public boolean needsRawInput() {
		return true;
	}

	protected BimBotsOutput toBimBotsJsonOutput(Object results, String outputDescription) throws BimBotsException {
		// convert output with Jackon
		byte[] ifcJsonResults;
		try {
			ObjectMapper mapper = new ObjectMapper();
			ifcJsonResults = mapper.writeValueAsBytes(results);

			BimBotsOutput output = new BimBotsOutput(getOutputSchema(), ifcJsonResults);
			output.setContentType("application/json");
			output.setTitle(outputDescription);
			return output;

		} catch (JsonProcessingException e) {
			throw new BimBotsException("Unable to convert retrieved objects to Json", 500);
		}
	}

	public ObjectStore getStore() {
		return store;
	}

	public void setStore(ObjectStore store) {
		this.store = store;
	}


}