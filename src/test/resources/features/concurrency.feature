Feature: Concurrency
    User can run multiple tasks simultaneously.

    Scenario: Run two task in same tame
        Given user has logged in with username "test" and password "1234"
        When user starts command "submit path " and path "/testResources/2013_ohpeJaOhja/viikko1" and exercise "Viikko1_002.HeiMaailma" and then user starts command help
        Then user A gets output of submit and user B gets output of help
    
    Scenario: Run a shorter task and a longer task at the same time
        Given user has logged in with username "test" and password "1234"
        When user starts command "submit path " and path "/testResources/2013_ohpeJaOhja/viikko1" and exercise "Viikko1_002.HeiMaailma" and then user starts command help
        Then user A gets output after user B
