package com.microsoft.alm.plugin.context.rest.deserialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.alm.sourcecontrol.webapi.model.GitChange;
import com.microsoft.alm.sourcecontrol.webapi.model.GitCommitDiffs;

import java.io.IOException;

public class GitCommitDiffsDeserializer extends JsonDeserializer<GitCommitDiffs> {
    @Override
    public GitCommitDiffs deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        // changeCounts entfernen
        if (node.has("changeCounts")) {
            ((ObjectNode) node).remove("changeCounts");
        }

        // Neuen Mapper ohne das Custom-Modul nutzen!
        ObjectMapper vanillaMapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(GitChange.class, new GitChangeDeserializer());
        vanillaMapper.registerModule(module);
        return vanillaMapper.treeToValue(node, GitCommitDiffs.class);
    }
}
