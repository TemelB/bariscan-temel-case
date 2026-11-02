@ui @careers @qualityAssuranceCareers
Feature: Careers and Quality Assurance applications
  Visitors can explore teams and open the application form for a QA role.

  @p1 @qualityAssuranceApplication @positive @knownIssue
  Scenario: Verify that Quality Assurance jobs lead to the Lever application form
    Given I open the Insider One home page
    Then all main home page blocks should be loaded
    When I open the careers page
    And I expand all teams
    And I open the 'Quality Assurance' positions
    Then every listed job should match:
      | position   | Quality Assurance |
      | department | Quality Assurance |
      | location   | Istanbul, Turkey  |
    When I click Apply for the first listed job
    Then the Lever application form should be displayed

  # Same navigation and validation stages, using a different department/location.
  @p2 @availableJobApplication @alternative @positive
  Scenario: Verify that Sales Operations jobs lead to the matching Lever application form
    Given I open the Insider One home page
    Then all main home page blocks should be loaded
    When I open the careers page
    And I expand all teams
    And I open the 'Sales Operations' positions
    Then every listed job should match:
      | position   | Messaging Operations |
      | department | Sales Operations     |
      | location   | Sao Paulo, Brazil    |
    When I click Apply for the first listed job
    Then the Lever application form should be displayed
    And the application form should belong to the selected job

  # Additional UI scenario generated with AI assistance and refined after review (prompts/01-ui-teams-toggle.md).
  @p2 @teamsToggle @aiGenerated @positive
  Scenario: Verify that team cards stay consistent and link to Lever after collapsing and expanding again
    Given I open the careers page
    When I expand all teams
    Then every team card should be listed once with its Lever positions link
    When I collapse the teams
    Then only the initially visible team cards should remain
    When I expand all teams
    Then the same team cards should be visible again
