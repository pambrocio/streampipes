package de.fzi.sepa.model.client.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.fzi.cep.sepa.model.client.ontology.EnumeratedRange;
import de.fzi.cep.sepa.model.client.ontology.PrimitiveRange;
import de.fzi.cep.sepa.model.client.ontology.QuantitativeValueRange;
import de.fzi.cep.sepa.model.client.ontology.Range;
import de.fzi.cep.sepa.model.client.ontology.RangeType;

public class RangeSerializer implements JsonSerializer<Range>, JsonDeserializer<Range> {

	public JsonElement serialize(Range src, Type typeOfSrc, JsonSerializationContext context) {
        
    	return context.serialize(src, src.getClass());

    }
 
    public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
       
    	JsonObject jsonObject = json.getAsJsonObject();
        String rangeType = jsonObject.get("rangeType").getAsString();
 
        RangeType rt = RangeType.valueOf(rangeType);
		if (rt == RangeType.ENUMERATION) return context.deserialize(jsonObject, EnumeratedRange.class);
		else if (rt == RangeType.QUANTITATIVE_VALUE) return context.deserialize(jsonObject, QuantitativeValueRange.class);
		else return context.deserialize(jsonObject, PrimitiveRange.class);
    }

}

