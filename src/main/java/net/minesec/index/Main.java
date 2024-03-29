package net.minesec.index;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Copyright (c) 30/06/2017, MineSec. All rights reserved.
 */
class Main {

    private static final JsonFactory YAML = new YAMLFactory();
    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(YAML);

    public static void main(String[] args) {
        try (JsonGenerator generator = YAML.createGenerator(System.out, JsonEncoding.UTF8)) {
            generator.setCodec(YAML_MAPPER);
            generator.writeStartArray();
            Consumer<BugBounty> bugBountyConsumer = bounty -> {
                try {
                    generator.writeObject(bounty);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            };
            BugBountyIndexer.indexBugcrowdCurated(bugBountyConsumer);
            BugBountyIndexer.indexBugcrowdPrograms(bugBountyConsumer);
            BugBountyIndexer.indexVulnerabilityLabCurated(bugBountyConsumer);
            BugBountyIndexer.indexHackeroneCurated(bugBountyConsumer);
            generator.writeEndArray();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
