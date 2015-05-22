Feature: Logout
    User can logout.

    Scenario: Logout when logged in.
        Given a logout command.
        When user sees message.
        Then user data should be cleared.
    Scenario: Logout when no user data is present.
        Given a logout command without being logged in.
        When nothing should happen.
        Then user sees error message.