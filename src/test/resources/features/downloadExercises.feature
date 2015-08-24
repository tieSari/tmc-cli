Feature: Downloading exercises -command
    User can download exercises by specifying course id. 

    Scenario: Download exercises 
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command and course id.
        Then output should contain zip files and folders containing unzipped files
        And .zip -files are removed.
        And output should contain "153".

    Scenario: Download exercises with a course name
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command and course name.
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
        Then output should contain "Unable to download exercises: unable to identify course.".

    Scenario: Download exercises with a wrong course name
        Given user has logged in with username "pihla" and password "juuh".
        When user gives a download exercises command with a course name not on the server
        Then output should contain "There is no course with name notacourse on the server http://127.0.0.1:8080".
