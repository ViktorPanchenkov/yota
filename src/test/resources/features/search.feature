Feature: Find Sneakers


  Scenario: Search Product
    Given I Open browzer and navigate to Store Page
    When I Enter text in Search Filed
    When I Set Sneakers type
    And I Set MinPrice Filter
    And I Click on the search button
    Then I should See List of Sneakers