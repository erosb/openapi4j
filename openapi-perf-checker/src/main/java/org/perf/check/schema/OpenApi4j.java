package org.perf.check.schema;

import com.fasterxml.jackson.databind.JsonNode;

import org.openapi4j.core.exception.ResolutionException;
import org.openapi4j.core.validation.ValidationResults;
import org.openapi4j.schema.validator.ValidationData;

class OpenApi4j implements JsonValidator {

  OpenApi4j(JsonNode schema) throws ResolutionException {
//    schemaValidator = new SchemaValidator("schemas", schema);
  }

  @Override
  public String validate(JsonNode data) {
    ValidationData<Void> validation = new ValidationData<>();
//    schemaValidator.validate(data, validation);
    if (!validation.isValid()) {
      return validation.toString();
    }

    return null;
  }

  @Override
  public String getVersion() {
    return ValidationResults.class.getPackage().getImplementationVersion();
  }
}
