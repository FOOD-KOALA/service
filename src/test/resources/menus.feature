@E2E
Feature: managementul meniului
  Scenario: clientul creeaza si apoi sterge un meniu
    When clientul creeaza un meniu numit "Burger VIP" cu pretul 60.0
    Then statusul raspunsului este 201
    When clientul sterge meniul proaspat creat
    Then statusul raspunsului este 204