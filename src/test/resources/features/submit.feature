Feature: Submit command
    User can submit exercise to TMC-server.

    Scenario: submit doesn't work if exercise is expired
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with path "/testResources/k2015-tira/viikko01" and exercise "tira1.1"
        And user executes the command
        Then user will see a message which tells that exercise is expired.


