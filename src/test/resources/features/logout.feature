Feature: Logout
    User can logout.

    Scenario: Logout when logged in.
        Given logout command.
        When user sees message "cleared"
        Then user data should be cleared.