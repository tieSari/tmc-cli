Feature: ListExercises command
    User can list exercises of course belonging to current directory.

    Scenario: List exercises with credentials
        Given user has logged in with username "test" and password "1234".
        When user gives command listExercises with path "testResources/2013_ohpeJaOhja/viikko1/Viikko1_002.HeiMaailma".
        Then output should contain more than one line

    Scenario: List exercises without credentials
        Given user has not logged in
        When user gives command listExercises with path "testResources/2013_ohpeJaOhja/viikko1/Viikko1_002.HeiMaailma".
        Then exception should be thrown
       