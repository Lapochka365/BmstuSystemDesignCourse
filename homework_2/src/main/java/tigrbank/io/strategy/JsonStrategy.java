package tigrbank.io.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

@Component
public class JsonStrategy extends AbstractJacksonStrategy {
    public JsonStrategy() {
        super(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT));
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
