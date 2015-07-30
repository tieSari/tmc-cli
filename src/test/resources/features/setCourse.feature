Feature: SetCourse command
  User can save current course to session from cli.

  Scenario: SetCourse works with id
    Given user has logged in with username "test" and password "1234"
    When user gives command "setCourse course 21"
    Then user will see result " is set as current course."

  Scenario: SetCourse works with courseName
    Given user has logged in with username "test" and password "1234"
    When user gives command "setCourse course k2015-tira"
    Then user will see result " is set as current course."

  Scenario: SetCourse informs if unknown course
    Given user has logged in with username "test" and password "1234"
    When user gives command "setCourse course 2019_super_kvantti_mooc"
    Then user will see result "There is no course with name"