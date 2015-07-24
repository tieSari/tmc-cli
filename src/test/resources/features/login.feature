Feature: Authentication
    User can login.

    Scenario: Login success when server returns status 200.
        When user gives username "test" and password "1234" and status 200
        Then user should see result "Saved userdata in session"


    Scenario: Login fails when server returns status 400.
        When user gives username "wrong" and password "pasdjaklds" and status 400
        Then user should see result "Auth unsuccessful"