Feature: Submit command
    User can submit exercise to TMC-server.

    Scenario: submit works from exercise folder
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/2013_ohpeJaOhja/viikko1" and exercise "Viikko1_002.HeiMaailma"
        Then user will see all test passing

    Scenario: submit works from exercises source folder
        Given user has logged in with username "test" and password "1234"
        When user gives command submit with valid path "/testResources/failingExercise/viikko1/src" and exercise "Viikko1_001.Nimi"
<<<<<<< HEAD
        Then user will see the some test passing
        Then user will see the result of tests
=======
        Then user will see the some test passing
>>>>>>> a3f2f9be92426cd89883a869c76a9f187e20a8b1
