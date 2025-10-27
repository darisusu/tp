package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;

/**
 * Converts a Java object instance to JSON and vice versa.
 */
public class JsonUtil {

    private static final Logger logger = LogsCenter.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // === Match original AB3 Jackson 2.7 behavior ===
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // Keep original insertion order for both fields and map keys
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);

        // Register time module
        objectMapper.registerModule(new JavaTimeModule());

        // Custom serializers/deserializers
        SimpleModule customModule = new SimpleModule("CustomModule")
                .addSerializer(Level.class, new ToStringSerializer())
                .addDeserializer(Level.class, new LevelDeserializer(Level.class))
                // Keep relative filenames (fix ConfigUtilTest)
                .addSerializer(Path.class, new JsonSerializer<Path>() {
                    @Override
                    public void serialize(Path value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        gen.writeString(value.getFileName().toString());
                    }
                })
                .addDeserializer(Path.class, new JsonDeserializer<Path>() {
                    @Override
                    public Path deserialize(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        return Path.of(p.getValueAsString());
                    }
                });

        objectMapper.registerModule(customModule);
    }

    static <T> void serializeObjectToJsonFile(Path jsonFile, T objectToSerialize) throws IOException {
        FileUtil.writeToFile(jsonFile, toJsonString(objectToSerialize));
    }

    static <T> T deserializeObjectFromJsonFile(Path jsonFile, Class<T> classOfObjectToDeserialize)
            throws IOException {
        return fromJsonString(FileUtil.readFromFile(jsonFile), classOfObjectToDeserialize);
    }

    /**
     * Returns the JSON object from the given file or {@code Optional.empty()} if the file is not found.
     */
    public static <T> Optional<T> readJsonFile(Path filePath, Class<T> classOfObjectToDeserialize)
            throws DataLoadingException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        logger.info("JSON file " + filePath + " found.");

        try {
            return Optional.of(deserializeObjectFromJsonFile(filePath, classOfObjectToDeserialize));
        } catch (IOException e) {
            logger.warning("Error reading from jsonFile file " + filePath + ": " + e);
            throw new DataLoadingException(e);
        }
    }

    /**
     * Saves the JSON object to the specified file.
     */
    public static <T> void saveJsonFile(T jsonFile, Path filePath) throws IOException {
        requireNonNull(filePath);
        requireNonNull(jsonFile);
        serializeObjectToJsonFile(filePath, jsonFile);
    }

    /**
     * Converts a JSON string to an instance of a class.
     */
    public static <T> T fromJsonString(String json, Class<T> instanceClass) throws IOException {
        return objectMapper.readValue(json, instanceClass);
    }

    /**
     * Converts a class instance to its JSON string representation.
     */
    public static <T> String toJsonString(T instance) throws JsonProcessingException {
        // Force compact JSON by disabling indentation
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(instance);
    }

    /**
     * Deserializer for java.util.logging.Level.
     */
    private static class LevelDeserializer extends FromStringDeserializer<Level> {
        protected LevelDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        protected Level _deserialize(String value, DeserializationContext ctxt) {
            return Level.parse(value);
        }

        @Override
        public Class<Level> handledType() {
            return Level.class;
        }
    }
}
