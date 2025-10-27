package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
        objectMapper.findAndRegisterModules();

        // Preserve declaration order (no alphabetic sorting)
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);

        // ENABLE pretty printing to match expected JSON_STRING_REPRESENTATION
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        objectMapper.registerModule(new JavaTimeModule());
        SimpleModule customModule = new SimpleModule("CustomModule")
                .addSerializer(Level.class, new ToStringSerializer())
                .addDeserializer(Level.class, new LevelDeserializer(Level.class))
                .addSerializer(Path.class, new PathSerializer())
                .addDeserializer(Path.class, new PathDeserializer());
        objectMapper.registerModule(customModule);
    }

    /**
     * Writes an object as JSON to the specified file.
     */
    static <T> void serializeObjectToJsonFile(Path jsonFile, T objectToSerialize) throws IOException {
        FileUtil.writeToFile(jsonFile, toJsonString(objectToSerialize));
    }

    /**
     * Reads and converts a JSON file to an object of the given class.
     */
    static <T> T deserializeObjectFromJsonFile(Path jsonFile, Class<T> classOfObjectToDeserialize)
            throws IOException {
        return fromJsonString(FileUtil.readFromFile(jsonFile), classOfObjectToDeserialize);
    }

    /**
     * Reads a JSON file into an object, returning Optional.empty() if the file does not exist.
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
     * Saves the given object as a JSON file.
     */
    public static <T> void saveJsonFile(T jsonFile, Path filePath) throws IOException {
        requireNonNull(filePath);
        requireNonNull(jsonFile);
        serializeObjectToJsonFile(filePath, jsonFile);
    }

    /**
     * Converts a JSON string to an object of the given class.
     */
    public static <T> T fromJsonString(String json, Class<T> instanceClass) throws IOException {
        return objectMapper.readValue(json, instanceClass);
    }


    /**
     * Converts an object to its JSON string representation without pretty-printing.
     */
    public static <T> String toJsonString(T instance) throws JsonProcessingException {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator gen = objectMapper.getFactory().createGenerator(sw);
            gen.useDefaultPrettyPrinter(); // enables nice formatting
            objectMapper.writeValue(gen, instance);
            gen.close();
            return sw.toString().trim();
        } catch (IOException e) {
            throw new JsonProcessingException("serialization failed", e) {};
        }
    }


    /** Custom deserializer for java.util.logging.Level. */
    private static class LevelDeserializer extends
            com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer<Level> {
        protected LevelDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Level deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
            return Level.parse(parser.getValueAsString());
        }
    }

    /** Custom serializer for java.nio.file.Path that stores only the filename. */
    private static class PathSerializer extends com.fasterxml.jackson.databind.ser.std.StdScalarSerializer<Path> {
        protected PathSerializer() {
            super(Path.class);
        }

        @Override
        public void serialize(Path value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.toString());
        }
    }

    /** Custom deserializer for java.nio.file.Path. */
    private static class PathDeserializer extends com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer<Path> {
        protected PathDeserializer() {
            super(Path.class);
        }

        @Override
        public Path deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
            String value = parser.getValueAsString();
            try {
                return Paths.get(value);
            } catch (InvalidPathException ex) {
                throw InvalidFormatException.from(parser, "Invalid path", value, Path.class);
            }
        }
    }
}
