Feature: Submit command
    User can submit exercise to TMC-server.

    Scenario: submit works from exercise folder
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/2013_ohpeJaOhja/viikko1" and exercise "Viikko1_002.HeiMaailma"
        Then user will see all test passing

    Scenario: submit works from exercises source folder
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/failingExercise/viikko1" and exercise "Viikko1_001.Nimi/src"
        Then user will see the some test passing

    Scenario: submit doesn't work if exercise is expired
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with expired path "/testResources/k2015-tira/viikko01" and exercise "tira1.1"
        Then user will see a message which tells that exercise is expired.
