package Eco_Log.Eco_Log.tool;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if(Objects.isNull(stringList)){
            return "";
        }else{
            return String.join(SPLIT_CHAR, stringList);
        }

    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if(Objects.isNull(string)){
            return Collections.emptyList();
        }else{
            return Arrays.asList(string.split(SPLIT_CHAR));
        }

    }
}