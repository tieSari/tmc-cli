Feature: Logout
    User can logout.

    Scenario: Logout.
        Given a logout command.
        Then user data should be cleared.
        Then user sees message.