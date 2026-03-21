package tigrbank.io.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Component;

@Component
public class YamlStrategy extends AbstractJacksonStrategy {
    public YamlStrategy() {
        super(new ObjectMapper(new YAMLFactory())
                .enable(SerializationFeature.INDENT_OUTPUT));
    }

    @Override
    public String getFormat() {
        return "yaml";
    }
}
