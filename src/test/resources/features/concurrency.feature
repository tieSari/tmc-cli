Feature: Concurrency
    User can run multiple tasks simultaneously.

    Scenario: Run two task in same tame
        Given user has logged in with username "test" and password "1234"
        When user starts command "submit path " and path "/testResources/2013_ohpeJaOhja/viikko1" and exercise "Viikko1_002.HeiMaailma" and then user starts command help
        Then user sees first output of help and after that submit