@api @pet @petValidation
Feature: Petstore rejects missing pets and invalid input
  Error responses are checked against explicit expectations.

  Background:
    Given I configure the Petstore API client

  @p2 @readMissingPet @negative
  Scenario: Verify that retrieving a nonexistent pet returns a not found response
    Given I have a unique unused pet ID
    When I retrieve my pet
    Then the response status should be 404
    And the response message should be 'Pet not found'

  @p2 @deleteMissingPet @negative
  Scenario: Verify that deleting a nonexistent pet returns a not found response
    Given I have a unique unused pet ID
    When I delete my pet
    Then the response status should be 404

  @p2 @readInvalidPetId @contract @negative @knownIssue
  Scenario: Verify that retrieving a pet with an invalid ID returns the observed not found status
    When I retrieve a pet with invalid ID 'invalid-id'
    Then the response status should be 404

  @p2 @deleteInvalidPetId @contract @negative @knownIssue
  Scenario: Verify that deleting a pet with an invalid ID returns the observed not found status
    When I delete a pet with invalid ID 'invalid-id'
    Then the response status should be 404

  @p2 @createPetInvalidJson @negative
  Scenario: Verify that creating a pet with malformed JSON returns a bad input error
    When I attempt to create a pet with malformed JSON
    Then the response status should be 400
    And the response content type should be JSON
    And the response error type should be 'unknown'
    And the response message should be 'bad input'

  @p2 @updatePetInvalidJson @negative
  Scenario: Verify that updating a pet with malformed JSON returns a bad input error
    When I attempt to update a pet with malformed JSON
    Then the response status should be 400
    And the response content type should be JSON
    And the response error type should be 'unknown'
    And the response message should be 'bad input'

  @p2 @updateMissingPetForm @negative
  Scenario: Verify that form-updating a nonexistent pet returns a not found response
    Given I have a unique unused pet ID
    When I update my pet using form data with name 'ghost'
    Then the response status should be 404
    And the response message should be 'not found'
