@PaymentCRUD
Feature: Payment CRUD operations

  Scenario: Create new payment
    Given Payments to be saved has following details
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |
    When the endpoint to create new payments is hit with the following payment
      | id | amount | currency | productName |
      | 0  | 1000   | EUR      | Laptop      |
    Then the response code should be 200
    And the response should contain the following list of payments
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |
      | 3  | 1000   | EUR      | Laptop      |

  Scenario: Update Payment
    Given Payments to be saved has following details
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |
    When the endpoint to update payment with id "1" is hit with the following payment
      | id | amount | currency | productName |
      | 0  | 1000   | EUR      | Laptop      |
    Then the response code should be 200
    And the response should contain the following list of payments
      | id | amount | currency | productName |
      | 1  | 1000   | EUR      | Laptop      |
      | 2  | 3000   | USD      | TV          |

  Scenario: Delete payment
    Given Payments to be saved has following details
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |
      | 3  | 1000   | EUR      | Laptop      |
    When the endpoint to delete payment with id "3" is hit
    Then the response code should be 200
    When the endpoint to get all payments is hit
    Then the response code should be 200
    And the response should contain the following list of payments
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |

  Scenario: Get All Payments
    Given Payments to be saved has following details
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |
    When the endpoint to get all payments is hit
    Then the response code should be 200
    And the response should contain the following list of payments
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |

  Scenario: Get payments By ID
    Given Payments to be saved has following details
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |
      | 2  | 3000   | USD      | TV          |
    When the endpoint to get payment with id "1" is hit
    Then the response code should be 200
    And the response should contain the following list of payments
      | id | amount | currency | productName |
      | 1  | 2000   | GBP      | Mobile      |


