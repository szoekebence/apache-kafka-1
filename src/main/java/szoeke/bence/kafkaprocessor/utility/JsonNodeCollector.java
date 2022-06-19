package szoeke.bence.kafkaprocessor.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class JsonNodeCollector implements Collector<JsonNode, ArrayNode, ArrayNode> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Supplier<ArrayNode> supplier() {
        return mapper::createArrayNode;
    }

    @Override
    public BiConsumer<ArrayNode, JsonNode> accumulator() {
        return ArrayNode::add;
    }

    @Override
    public BinaryOperator<ArrayNode> combiner() {
        return (x, y) -> {
            x.addAll(y);
            return x;
        };
    }

    @Override
    public Function<ArrayNode, ArrayNode> finisher() {
        return accumulator -> accumulator;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

    public static JsonNodeCollector toJsonNode() {
        return new JsonNodeCollector();
    }
}
