@UC_NGS_ASM01

Feature: User Registration and Login

  @UC_NGS_ASM01_User_Registration_and_Login
  @TC_NGS_ASM01_01
  Scenario: User Registration and Login
    Given Open the "JPetStore Demo" website
    And Navigate to the registration page
    Then Fill in the registration form with the following details:
      |        User_ID|Password|First_name|    Last_name|                            Email|     Phone|Address_1| Address_2|   City|     State|     Zip| Country|Language|Category|MyList|Banner|
      |j_aungkulanavi5|  asxxxx|   Jirxxxx|Aungxxxxxxxxx|jirapat.aungkulanavin@koerber.co4|1959859512|    128/2|Building B|Bangkok|TalingChan|101xx   |Thailand| english|    CATS|  true| false|
    And Submit the registration form
    When Login with new account
      |        User_ID|Password|
      |j_aungkulanavi5|  asxxxx|
    Then Home page after login

