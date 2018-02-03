package spring2018.controller;

import io.restassured.RestAssured;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class IndexControllerTest {

  @Test
  public void welcome() throws Exception {
    System.out.println("Welcome! Test Successful");
  }

  @Test
  public void testGetHomePage() throws URISyntaxException {
    RestAssured.when().get(new URI("http://localhost:8080/")).then().statusCode(200);
  }

}
