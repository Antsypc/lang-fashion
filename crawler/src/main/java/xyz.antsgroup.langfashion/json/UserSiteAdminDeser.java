package xyz.antsgroup.langfashion.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * For jackson parse, bool map to integer on site_admin.It means 1 while the user is site admin.
 *
 * @author ants_ypc
 * @version 1.0 4/8/16
 */
public class UserSiteAdminDeser extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        boolean value = jsonParser.getBooleanValue();
        return value ? 1 : 0;
    }
}
