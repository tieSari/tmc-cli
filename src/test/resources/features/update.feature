Feature: Update command
    The user can update exercises.

    Scenario: Download updates
        Given changed and new exercises
        When the user gives the update command
        Then the updates should be downloaded
        And the user should see how many updates were downloaded

    Scenario: Does not download anything if there are no new updates
        Given there are no updates
        When the user gives the update command
        Then the output should say no updates were downloaded
        And no downloads should have happened

    Scenario: Without a course nothing will happen
        Given the course cannot be determined
        When the user gives the update command
        Then the user will see the error message "blaa"
        
    Scenario: Without logging in nothing will happen
        Given the user has not logged in
        When the user gives the update command
        Then the user will see the error message "blee"