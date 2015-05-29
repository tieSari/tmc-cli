Feature: Submit command
    User can submit exercise to TMC-server.

    Scenario: submit works from exercise folder
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/2013_ohpeJaOhja/viikko1" and exercise "Viikko1_002.HeiMaailma"
        Then user will see the result of tests