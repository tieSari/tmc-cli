Feature: Help command
    User can list available commands.

    Scenario: List commands
        Given a help command.
        Then output should contains commands.