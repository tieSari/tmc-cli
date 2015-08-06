Feature: ListCourses command
    User can list available courses on TMC-server.

    Scenario: List courses with credentials
        Given user has logged in with username "test" and password "1234".
        When user gives command listCourses.
        Then output should contain "ID"

    Scenario: List courses without credentials
        Given user has not logged in
        When user gives command listCourses.
        Then output should contain "Please authorize first."