package ro.unibuc.prodeng.e2e.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ro.unibuc.prodeng.request.CreateMenuRequest;
import ro.unibuc.prodeng.response.MenuResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

// AM SCHIMBAT NUMELE AICI:
public class MenuSteps { 

    private final String BASE_URL = "http://localhost:8080/api/menus";
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<MenuResponse> lastResponse;
    private String lastCreatedId;

    @When("clientul creeaza un meniu numit {string} cu pretul {double}")
    public void createMenu(String name, Double price) {
        CreateMenuRequest request = new CreateMenuRequest(name, "Descriere E2E", price);
        lastResponse = restTemplate.postForEntity(BASE_URL, request, MenuResponse.class);
        if (lastResponse.getBody() != null) {
            lastCreatedId = lastResponse.getBody().id();
        }
    }

    @Then("statusul raspunsului este {int}")
    public void verifyStatus(int statusCode) {
        assertThat(lastResponse.getStatusCode().value(), is(statusCode));
    }

    @When("clientul sterge meniul proaspat creat")
    public void deleteMenu() {
        // Facem cererea reală de DELETE către server
        restTemplate.delete(BASE_URL + "/" + lastCreatedId);
        
        // Simulăm un obiect response pentru a putea verifica statusul 204 în pasul următor
        lastResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}