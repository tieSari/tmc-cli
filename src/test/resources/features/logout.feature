Feature: Logout
    User can logout.

    Scenario: Logout when logged in.
        Given a logout command.
        Then user data should be cleared.
        Then user sees message.
    Scenario: Logout when no user data is present.
        Given a logout command without being logged in.
        Then nothing should happen.
        Then user sees error message.