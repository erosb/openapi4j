package org.openapi4j.operation.validator.validation.operation;

import org.junit.Test;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.core.validation.ValidationResult;
import org.openapi4j.core.validation.ValidationResults;
import org.openapi4j.operation.validator.model.impl.Body;
import org.openapi4j.operation.validator.model.impl.DefaultRequest;
import org.openapi4j.operation.validator.model.impl.DefaultResponse;
import org.openapi4j.operation.validator.validation.RequestValidator;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.openapi4j.operation.validator.model.Request.Method.GET;

public class UsersApiTest extends OperationValidatorTestBase {

  @Test
  public void testRequest() throws Exception {
    URL specPath = getClass().getResource("/users/users-api.yaml");
    OpenApi3 api = new OpenApi3Parser().parse(specPath, false);

    DefaultRequest request = new DefaultRequest.Builder("/users", GET).build();
    new RequestValidator(api).validate(request);

    DefaultResponse resp = new DefaultResponse.Builder(200)
      .header("Content-Type", "application/json")
      .body(Body.from("[{\"id\":2}]"))
      .build();

    new RequestValidator(api).validate(resp, request);

    DefaultResponse invalidResp = new DefaultResponse.Builder(200)
      .header("Content-Type", "application/json")
      .body(Body.from("[{\"id\":-5}, {\"userId\":4}]"))
      .build();

    try {
      new RequestValidator(api).validate(invalidResp, request);
    } catch (ValidationException e) {
      ValidationResults results = e.results();
      List<String> actualMessages = results.items().stream().map(ValidationResult::message).collect(toList());
      assertEquals(Arrays.asList("-5 is lower than minimum 0",
        "actual instance is not the same as expected constant value"), actualMessages);
      results.items().forEach(item -> {
        System.out.println(item.message() + "--" + item.schemaCrumbs()
          + "\n" + item.dataCrumbs() + "\n" + item.toString()
        );
      });
    }
  }

  @Test
  public void testVersionsEndpoint() throws Exception {
    URL specPath = getClass().getResource("/users/users-api.yaml");
    OpenApi3 api = new OpenApi3Parser().parse(specPath, false);

    DefaultRequest request = new DefaultRequest.Builder("/users/versions", GET).build();
    new RequestValidator(api).validate(request);
  }
}
