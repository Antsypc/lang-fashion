package xyz.antsgroup.langfashion.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * For jackson parse,get the second of datetime which format such as yyyy-MM-dd'T'HH:mm:ss'Z'.
 *
 * @author ants_ypc
 * @version 1.0 4/8/16
 */
public class DateTimeDeser extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(value);
        } catch (ParseException e) {
            date = new Date(0);
        }
        return (int)(date.getTime() / 1000);
    }
}
