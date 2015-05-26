Feature: Downloading exercises -command
    User can download exercises by specifying course id. 

    Scenario: Download exercises 
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command and course id.
        Then output should contain zip files and folders containing unzipped files
        And information about download progress.