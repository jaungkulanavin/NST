@UC_NGS_ASM02

Feature: Product Search

  @UC_NGS_ASM02_Product_Search
  @TC_NGS_ASM02_01
  Scenario: Navigate to the catalog page
    Given Open the "JPetStore Demo" website
    When Successful login
      |        User_ID|Password|
      |j_aungkulanavi5|  asxxxx|
    Then Home page after login

  @TC_NGS_ASM02_02
  Scenario Outline: Search & Validate for products using various keywords
    Given Catalog page
    When Search for product "<keyword>"
    Then Validate search results "<keyword>"
    #For product availability checking only the first row on each keyword
    And Validate product availability
    Examples:
    |keyword|
    |      a|
    |   fish|
    |   cat |
    |  dog  |


