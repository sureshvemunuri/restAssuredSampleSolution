package sampleRestassured.restassuredE2EFlowPOC;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AppTest {

	private RequestSpecification requestSpec;
	private RequestSpecBuilder builder;
	private Response response;
	private Vote vote;
	private int postedVoteId;
	private int deletedVoteId;
	private Vote randomVote;

	@BeforeClass
	public void setup() {
		// Set up common specifications like base url and base path
		RestAssured.baseURI = "https://api.thecatapi.com/v1/";
		RestAssured.basePath = "votes";

		// Creating object with common request specifications like headers and
		// content-type
		builder = new RequestSpecBuilder();
		builder.addHeader("x-api-key", "DEMO-API-KEY");
		builder.setContentType("application/json");
		requestSpec = builder.build();
	}

	@Test(priority = 1)
	public void getAllVotes() {

		response = given().spec(requestSpec) // using common specifications
				.get().then().assertThat().statusCode(200) // validate status code
				.body("$.size()", greaterThan(0)) // validate response body is not empty
				.extract().response();

//Extracting first vote randomly and save it class level
		Vote[] votes = response.body().as(Vote[].class);
		randomVote = votes[0];
		Assert.assertTrue(votes.length > 0); /* Validating response is not empty array or list */

		System.out.println("Test Case 1 PASS");
	}

	@Test(priority = 2)
	public void getRandomVote() {

// Extract the getVote response
		response = given().spec(requestSpec) // using common specifications
				.pathParam("pathParamGetVote", randomVote.getId()) // setting up path param
				.when().get("{pathParamGetVote}") // set up endpoint
				.then().body("$", not(empty())) // validate response body is not empty
				.extract().response();

// validate getVote api response status code
		Assert.assertEquals(response.statusCode(), 200);

// validate getVote api response content with previously stored response
		vote = response.getBody().as(Vote.class);
		Assert.assertEquals(vote.getId(), randomVote.getId());
		Assert.assertEquals(vote.getImage_id(), randomVote.getImage_id());
		Assert.assertEquals(vote.getSub_id(), randomVote.getSub_id());
		Assert.assertEquals(vote.getConutryCode(), randomVote.getConutryCode());
		Assert.assertEquals(vote.getValue(), randomVote.getValue());
		Assert.assertEquals(vote.getCreated_at(), randomVote.getCreated_at());

		System.out.println("Test Case 2 GetRandom PASS");

	}

	@Test(priority = 3)
	public void postVote() {

// Setting up post load
		Vote payload = new Vote();
		payload.setImage_id("adfx4");
		payload.setSub_id("my_user_4321");
		int number = (int) (Math.random() * 100);
		payload.setValue(number);

// Sending post vote api and verify status code and extracting response
		response = given().spec(requestSpec) // using common specifications
				.body(payload) // setting up post body payload
				.when().post().then().assertThat().statusCode(200) // validate status code
				.body("id", not(empty())) // validate id is not empty in response body
				.body("message", equalTo("SUCCESS")).extract().response();

		String message = response.getBody().jsonPath().get("message"); // extract message from response body
		Assert.assertEquals(message, "SUCCESS"); // validate message from post response

		postedVoteId = response.getBody().jsonPath().getInt("id"); // save newly posted vote it into global to access in
																	// next request

		System.out.println("Test Case 3 Post Vote PASS");

	}

	@Test(priority = 4)
	public void newlyPostedGetVote() {

// validate getVote for newly posted vote, status code and postedID
		response = given().spec(requestSpec) // using common specifications
				.pathParam("postedVoteId", postedVoteId) // setup path
				.when().get("{postedVoteId}") // sending get request
				.then().statusCode(200) // validate status code
				.body("id", equalTo(postedVoteId)) // validate id of get response with previously posted id
				.extract().response();

		System.out.println("Test Case 4 Get After New POst PASS");
	}

	@Test(priority = 5)
	public void deleteVote() {

		deletedVoteId = postedVoteId; // setting up deletedVote variable before deleteting vote

		given().spec(requestSpec) // using common specifications
				.pathParam("deletedVoteId", postedVoteId) // setup path to previously posted vote
				.when().delete("{deletedVoteId}") // setup endpoint
				.then().statusCode(200) // validate delete api and status code and message
				.body("message", equalTo("SUCCESS"));

		System.out.println("Test Case 5 Delete PASS");
	}

	@Test(priority = 6)
	public void getDeletedDVote() {

// Validate get api after deleting vote
		given().spec(requestSpec).pathParam("deletedVoteId", deletedVoteId) // set up path param
				.when().get("{deletedVoteId}") // set up endpoint
				.then().statusCode(404) // validate status code for get api for deleted vote
				.body("message", equalTo("NOT_FOUND")); // validate message for get deleted vote

		System.out.println("Test Case 6 Get After Delete PASS");

	}

}