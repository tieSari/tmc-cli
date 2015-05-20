Feature: ListCourses command
    User can list available courses on TMC-server.

    Scenario: List courses without credentials
        Given user has run shutdown script.
        When user gives command listCourses.
        Then output should contain only one line.

    Scenario: List courses with credentials
        Given user has logged in with username "test" and password "1234".
        When user gives command listCourses.
        Then output should contain more than one line