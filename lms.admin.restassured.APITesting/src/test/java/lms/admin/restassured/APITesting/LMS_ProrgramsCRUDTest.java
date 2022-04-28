package lms.admin.restassured.APITesting;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class LMS_ProrgramsCRUDTest {

	private static Response response;
	private static final String BASE_URL = "https://lms-admin-rest-service.herokuapp.com";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "password";
	private static int programId = 0;

	@Test
	public void GETAllProgram() {
		String programId = response.jsonPath().getString("programId[0]");
		Assert.assertNotNull(programId);
	}
 
	@Test
	public void POSTProgram() {
		JSONObject request = new JSONObject();
		request.put("programName", "cucumber learning");
		request.put("programDescription", "cucumber learning");
		request.put("isOnline", false);
		response = given().auth().basic(USERNAME, PASSWORD).header("Content-type", "application/json").and()
				.body(request).when().post(BASE_URL + "/programs").then().extract().response();
		System.out.println("Response Body is: " + response.getBody().asString());
		validatePOSTrecord();
	}

	public void validatePOSTrecord() {
		JsonPath jsonPathEvaluator = response.jsonPath();
		Integer apiProgramId = jsonPathEvaluator.get("programId");
		Assert.assertNotNull(apiProgramId);
		programId = apiProgramId.intValue();
	}

	@Test
	public void GETRecordforProgramID() {
		response = given().auth().basic("admin", "password").
				when().get(BASE_URL + "/programs/" + programId);
		System.out.println(response.asString());
		ValidateProgramIDRecord();
	}
	public void ValidateProgramIDRecord() {
		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		// Then simply query the JsonPath object to get a String value of the node
		// specified by JsonPath: programId (Note: You should not put $. in the Java
		// code)
		Integer apiProgramId = jsonPathEvaluator.get("programId");
		// Validate the response
		Assert.assertNotNull(apiProgramId);
		Assert.assertTrue(programId == apiProgramId.intValue());
	}
	@Test
	public void PUTprogram() {
		JSONObject request = new JSONObject();
		request.put("programName", "cucumber learning modified");
		request.put("programDescription", "cucumber learning");
		request.put("isOnline", false);
		response = given().auth().basic("admin", "password").header("Content-type", "application/json").and()
				.body(request).when().put(BASE_URL + "/programs/" + programId).then().extract().response();
		System.out.println("authorized_user_update_program Response Body is: " + response.getBody().asString());
		validatePUT();
	}
	public void validatePUT() {
		JsonPath jsonPathEvaluator = response.jsonPath();
		// Then simply query the JsonPath object to get a String value of the node
		// specified by JsonPath: programId (Note: You should not put $. in the Java
		// code)
		String programNameUdt = jsonPathEvaluator.get("programName");
		System.out.println("From APi " + programNameUdt);
		Assert.assertEquals("cucumber learning modified", programNameUdt);
	}
	
	@Test
	public void deleteProgram() {
		response = given().auth().basic("admin", "password")
				.header("Content-type", "application/json")
				.when()
				.delete(BASE_URL + "/programs/" + programId)
				.then().extract().response();
		validateDelete();
	}
	
	public void validateDelete() {
		System.out.println("Response Body is: " + response.getBody().asString());
		int statusCode = response.getStatusCode();
		System.out.println(response.asString());
		Assert.assertEquals(statusCode, 200);
	}

	@BeforeTest
	public void beforeTest() {
		response = given().auth().basic(USERNAME, PASSWORD).when().get(BASE_URL + "/programs");
		Reporter.log("Valid test");
	}

	@AfterTest
	public void afterTest() {
	}

}
