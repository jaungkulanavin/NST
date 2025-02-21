@UC_NGS_ASM03

Feature: Adding/Removing Items from the Cart

  @UC_NGS_ASM03_Adding_Items_to_Cart
  @TC_NGS_ASM03_01
  Scenario: Navigate to the catalog page
    Given Open the "JPetStore Demo" website
    When Successful login
      |        User_ID|Password|
      |j_aungkulanavin|  asdfgh|
    Then Home page after login

  @TC_NGS_ASM03_02
  Scenario: Add item from the catalog page
    Given Catalog page
    When User select item from the side menu
    And User add item to the cart
    Then Selected item on the cart

  @TC_NGS_ASM03_03
  Scenario Outline: Add items from the search page
    Given Search for product "<keyword>"
    When User select item from the search
    And User add item from search to the cart
    Then Selected latest item on the cart
    Examples:
      |keyword|
      |      a|
      |    a  |
      |   fish|

  @TC_NGS_ASM03_04
  Scenario: Remove items from the cart & Validate the cart total and item list.
    Given Shopping cart page
    When The cart has items in list
    Then Validate the items from the cart
    And User remove the items from the cart
