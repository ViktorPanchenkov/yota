Feature: Find Sneakers


  Scenario: Search Product
    Given I Open browzer and navigate to Store Page
    When I Enter "adidas shoes men" in Search Filed
    When I Set Sneakers type
    And I Set MinPrice Filter
    And I Click on the search button
    Then I Should See Search result


  Scenario Outline: I Want to find different Products
    Given I Open browzer and navigate to Store Page
    When I Enter "<search text>" in Search Filed
    And I Set MinPrice Filter
    And I Click on the search button
    Then I Should See Search result

    Examples:
      | search text          |
      | puma shoes women |
      | Lacoste          |