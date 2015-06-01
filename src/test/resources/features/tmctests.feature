Feature: Test command
    The user can run the tests locally, and is shown the results.

    Scenario: Test an exercise where all tests pass
        Given the user is in the exercise directory "testResources/successExercise/viikko1/Viikko1_001.Nimi"
        When the user runs the tests
        Then the user sees that all tests have passed.

    Scenario: Test an exercise where all tests fail
        Given the user is in the exercise directory "testResources/failingExercise/viikko1/Viikko1_001.Nimi"
        When the user runs the tests
        Then the user sees which tests have failed

    Scenario: Test an exercise where some tests fail
<<<<<<< HEAD
        Given the user is in the exercise directory "testResources/halfdoneExercise/viikko1/Viikko1_015.TaysiIkaisyys"
=======
        Given the user is in the exercise directory "testResources/halfdoneExercise/viikko1/Viikko1_004.Muuttujat"
>>>>>>> 7e7a21132628c119da1f04ccfde7cba48da84eab
        When the user runs the tests
        Then the user sees both passed and failed tests