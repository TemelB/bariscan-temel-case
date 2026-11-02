@api @pet @petCrud
Feature: Petstore pet CRUD operations
  Each CRUD operation is independently executable and owns its test data.

  Background:
    Given I configure the Petstore API client

  @p1 @createPet @positive
  Scenario: Verify that a newly created pet is persisted with the correct details
    Given I have a unique unused pet ID
    When I create a pet named 'case-pet' with status 'available'
    Then the response status should be 200
    And the response should contain my pet ID
    And the pet response should have name 'case-pet' and status 'available'
    When I retrieve my pet
    Then the response status should be 200
    And the response should contain my pet ID
    And the pet response should have name 'case-pet' and status 'available'

  @p1 @readPet @positive
  Scenario: Verify that an existing pet can be retrieved with the correct details
    Given an existing pet named 'read-fixture' with status 'available'
    When I retrieve my pet
    Then the response status should be 200
    And the response should contain my pet ID
    And the pet response should have name 'read-fixture' and status 'available'

  @p1 @updatePet @positive
  Scenario: Verify that changes to an existing pet are persisted
    Given an existing pet named 'update-fixture' with status 'available'
    When I update my pet to name 'case-pet-updated' and status 'sold'
    Then the response status should be 200
    And the response should contain my pet ID
    And the pet response should have name 'case-pet-updated' and status 'sold'
    When I retrieve my pet
    Then the response status should be 200
    And the response should contain my pet ID
    And the pet response should have name 'case-pet-updated' and status 'sold'

  @p2 @updatePetForm @positive
  Scenario: Verify that a pet can be updated with form data
    Given an existing pet named 'form-fixture' with status 'available'
    When I update my pet using form data with name 'form-updated' and status 'sold'
    Then the response status should be 200
    And the response message should contain my pet ID
    When I retrieve my pet
    Then the response status should be 200
    And the pet response should have name 'form-updated' and status 'sold'

  @p2 @findPetsByStatus @positive
  Scenario: Verify that a pet is listed when searching by its status
    Given an existing pet named 'status-fixture' with status 'pending'
    When I search for pets with status 'pending'
    Then the response status should be 200
    And the pet list should contain my pet ID

  @p1 @deletePet @positive
  Scenario: Verify that a deleted pet can no longer be retrieved
    Given an existing pet named 'delete-fixture' with status 'available'
    When I delete my pet
    Then the response status should be 200
    And the response message should contain my pet ID
    When I retrieve my pet
    Then the response status should be 404
    And the response message should be 'Pet not found'
