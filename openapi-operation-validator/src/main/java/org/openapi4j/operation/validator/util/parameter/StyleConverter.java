package org.openapi4j.operation.validator.util.parameter;

import com.fasterxml.jackson.databind.JsonNode;

import org.openapi4j.core.exception.ResolutionException;
import org.openapi4j.operation.validator.util.TypeConverter;
import org.openapi4j.parser.model.v3.Parameter;
import org.openapi4j.parser.model.v3.Schema;

import java.util.Map;

import static org.openapi4j.core.model.v3.OAI3SchemaKeywords.TYPE_ARRAY;
import static org.openapi4j.core.model.v3.OAI3SchemaKeywords.TYPE_OBJECT;

interface StyleConverter {
  JsonNode convert(Parameter param, String rawValue) throws ResolutionException;

  default JsonNode convert(Parameter param, Map<String, Object> paramValues) {
    String style = param.getSchema().getSupposedType();
    Schema schema = param.getSchema();

    if (TYPE_OBJECT.equals(style)) {
      return TypeConverter.instance().convertObject(schema, paramValues);
    } else if (TYPE_ARRAY.equals(style)) {
      return TypeConverter.instance().convertArray(schema.getItemsSchema(), paramValues.get(param.getName()));
    } else {
      return TypeConverter.instance().convertPrimitiveType(schema, paramValues.get(param.getName()));
    }
  }
}