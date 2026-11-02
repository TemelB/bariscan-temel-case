package com.casestudy.stepdefs;

import com.casestudy.context.PetContext;
import com.casestudy.clients.PetClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.casestudy.fixtures.PetFixture;
import io.restassured.http.ContentType;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public final class PetStepdefs {
    private final PetContext context;
    private final PetFixture fixture;

    public PetStepdefs(PetContext context, PetFixture fixture) {
        this.context = context;
        this.fixture = fixture;
    }

    @Given("^I configure the Petstore API client$")
    public void givenIConfigureThePetstoreApiClient() {
        context.client = new PetClient();
    }

    @Given("^I have a unique unused pet ID$")
    public void givenIHaveAUniqueUnusedPetId() {
        context.setPetId(fixture.findUnusedId());
    }

    @Given("^an existing pet named '(.*)' with status '(.*)'$")
    public void givenAnExistingPetWithNameAndStatus(String name, String status) {
        fixture.createExistingPet(name, status);
    }

    @When("^I create a pet named '(.*)' with status '(.*)'$")
    public void whenICreateAPetWithNameAndStatus(String name, String status) {
        context.ownedIds.add(context.getPetId());
        context.response = context.client.create(context.getPetId(), name, status);
    }

    @When("^I update my pet to name '(.*)' and status '(.*)'$")
    public void whenIUpdateMyPetWithNameAndStatus(String name, String status) {
        context.response = context.client.update(context.getPetId(), name, status);
    }

    @When("^I retrieve my pet$")
    public void whenIRetrieveMyPet() {
        context.response = context.client.get(context.getPetId());
    }

    @When("^I delete my pet$")
    public void whenIDeleteMyPet() {
        context.response = context.client.delete(context.getPetId());
    }

    @When("^I search for pets with status '(.*)'$")
    public void whenISearchForPetsWithStatus(String status) {
        context.response = context.client.findByStatus(status);
    }

    @When("^I update my pet using form data with name '([^']*)' and status '(.*)'$")
    public void whenIUpdateMyPetUsingFormData(String name, String status) {
        context.response = context.client.updateWithForm(context.getPetId(),
                Map.of("name", name, "status", status));
    }

    @When("^I update my pet using form data with name '([^']*)'$")
    public void whenIUpdateMyPetNameUsingFormData(String name) {
        context.response = context.client.updateWithForm(context.getPetId(), Map.of("name", name));
    }

    @When("^I retrieve a pet with invalid ID '(.*)'$")
    public void whenIRetrieveAPetWithInvalidId(String id) {
        context.response = context.client.get(id);
    }

    @When("^I delete a pet with invalid ID '(.*)'$")
    public void whenIDeleteAPetWithInvalidId(String id) {
        context.response = context.client.delete(id);
    }

    @When("^I attempt to create a pet with malformed JSON$")
    public void whenIAttemptToCreateAPetWithMalformedJson() {
        context.response = context.client.createWithMalformedJson();
    }

    @When("^I attempt to update a pet with malformed JSON$")
    public void whenIAttemptToUpdateAPetWithMalformedJson() {
        context.response = context.client.updateWithMalformedJson();
    }

    @Then("^the response status should be ([0-9]+)$")
    public void thenTheResponseStatusShouldBe(int expectedStatusCode) {
        context.response.then()
                .statusCode(expectedStatusCode);
    }

    @Then("^the response should contain my pet ID$")
    public void thenTheResponseShouldContainMyPetId() {
        context.response.then()
                .body("id", equalTo(context.getPetId()));
    }

    @Then("^the pet response should have name '(.*)' and status '(.*)'$")
    public void thenThePetResponseShouldHaveNameAndStatus(String name, String status) {
        context.response.then()
                .body("name", equalTo(name), "status", equalTo(status));
    }

    @Then("^the response message should contain my pet ID$")
    public void thenTheResponseMessageShouldContainMyPetId() {
        context.response.then()
                .body("message", equalTo(Long.toString(context.getPetId())));
    }

    @Then("^the response message should be '(.*)'$")
    public void thenTheResponseMessageShouldBe(String message) {
        context.response.then()
                .body("message", equalTo(message));
    }

    @Then("^the response content type should be JSON$")
    public void thenTheResponseContentTypeShouldBeJson() {
        context.response.then()
                .contentType(ContentType.JSON);
    }

    @Then("^the response error type should be '(.*)'$")
    public void thenTheResponseErrorTypeShouldBe(String type) {
        context.response.then()
                .body("type", equalTo(type));
    }

    @Then("^the pet list should contain my pet ID$")
    public void thenThePetListShouldContainMyPetId() {
        context.response.then()
                .body("id", hasItem(context.getPetId()));
    }
}
