Feature: Concurrency
    User can run multiple tasks simultaneously.

    Scenario: Run two task in same tame
        Given user starts commands "ping" and "help"
        Then user sees output of both commands