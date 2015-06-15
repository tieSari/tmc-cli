Feature: Submit command
    User can submit exercise to TMC-server.

    Scenario: submit works from exercise folder
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/2013_ohpeJaOhja/viikko1"
        And exercise "Viikko1_002.HeiMaailma"
        And user executes the command
        Then user will see all test passing
        

    Scenario: submit works with vim too
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/2013_ohpeJaOhja/viikko1" 
        And exercise "Viikko1_002.HeiMaailma"
        And flag "--vim"
        And user executes the command
        Then user will see all test passing

    Scenario: submit doesn't work if exercise is expired
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with expired path "/testResources/k2015-tira/viikko01" and exercise "tira1.1"
        Then user will see a message which tells that exercise is expired.
