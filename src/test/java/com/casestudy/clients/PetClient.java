package com.casestudy.clients;

import com.casestudy.config.TestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.config.LogConfig;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;

public final class PetClient {
    private static final String MALFORMED_JSON = "{\"id\":";
    private final ByteArrayOutputStream traffic = new ByteArrayOutputStream();
    private final PrintStream trafficStream = new PrintStream(traffic, true, StandardCharsets.UTF_8);
    private final RequestSpecification api = new RequestSpecBuilder()
        .setBaseUri(TestConfig.get("api.baseUrl"))
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .addFilter(new RequestLoggingFilter(LogDetail.ALL, trafficStream))
        .addFilter(new ResponseLoggingFilter(LogDetail.ALL, trafficStream))
        .setConfig(io.restassured.config.RestAssuredConfig.config().httpClient(
            io.restassured.config.HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 15000).setParam("http.socket.timeout", 15000))
            .logConfig(LogConfig.logConfig().blacklistHeader("Authorization", "Cookie", "Set-Cookie", "X-API-Key")))
        .build();

    public String trafficLog() {
        return traffic.toString(StandardCharsets.UTF_8);
    }

    public Response create(long id, String name, String status) {
        return given().spec(api).body(petBody(id, name, status)).post("/pet");
    }

    public Response update(long id, String name, String status) {
        return given().spec(api).body(petBody(id, name, status)).put("/pet");
    }

    public Response get(long id) {
        return get(Long.toString(id));
    }

    public Response get(String id) {
        return given().spec(api).pathParam("id", id).get("/pet/{id}");
    }

    public Response delete(long id) {
        return delete(Long.toString(id));
    }

    public Response delete(String id) {
        return given().spec(api).pathParam("id", id).delete("/pet/{id}");
    }

    public Response updateWithForm(long id, Map<String, String> fields) {
        return given().spec(api)
                .contentType(ContentType.URLENC)
                .pathParam("id", id)
                .formParams(fields)
                .post("/pet/{id}");
    }

    public Response createWithMalformedJson() {
        return given().spec(api).body(MALFORMED_JSON).post("/pet");
    }

    public Response updateWithMalformedJson() {
        return given().spec(api).body(MALFORMED_JSON).put("/pet");
    }

    private Map<String, Object> petBody(long id, String name, String status) {
        return Map.of("id", id, "name", name,
                "photoUrls", List.of("https://example.com/pet.png"), "status", status);
    }

    public Response findByStatus(String status) {
        return given().spec(api)
            .queryParam("status", status)
            .get("/pet/findByStatus");
    }

}
