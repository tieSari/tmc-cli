Feature: Downloading exercises -command
    User can download exercises by specifying course id. 

    Scenario: Download exercises 
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command and course id.
        Then output should contain zip files and folders containing unzipped files
        And .zip -files are removed.
        And output should contain "153".

    Scenario: Download locked exercises
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command and course id with locked exercises.
        Then output should contain "152".

    Scenario: Download exercises with a wrong course id
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command and course id that isnt a real id.
        Then output should contain "Could not find the course. Please check your internet connection".
