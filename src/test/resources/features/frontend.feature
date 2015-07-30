Feature: Help command
    User can list available commands.

    Scenario: List commands
        Given help command.
        Then output should contains commands.

    Scenario: Show settings
        Given show settings command.
        Then output should contains settings information.