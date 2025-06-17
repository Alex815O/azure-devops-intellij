package com.microsoft.alm.plugin.context.rest.deserialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.alm.sourcecontrol.webapi.model.GitChange;

import java.io.IOException;

public class GitChangeDeserializer extends JsonDeserializer<GitChange> {
    @Override
    public GitChange deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        // changeCounts entfernen
        if (node.has("changeType")) {
            ((ObjectNode) node).remove("changeType");
        }

        // Neuen Mapper ohne das Custom-Modul nutzen!
        ObjectMapper vanillaMapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        return vanillaMapper.treeToValue(node, GitChange.class);
    }
}
