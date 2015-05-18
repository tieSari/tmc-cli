Feature: Authentication
    User can login.

    Scenario: Login.
        Given a login command.
        When user gives username "test"
        When user gives password "1234"
        Then user should be able to login.