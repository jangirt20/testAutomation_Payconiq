package com.booking;
import com.booking.client.BookingClient;
import com.booking.constants.Constants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BookingTestCases {
    BookingClient bookingClient = new BookingClient();

        @Test(priority = 1, description = "Verify the Post API to check booking is able to create")
        public void verifyThePostApiToCreateBooking() throws IOException, ParseException {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/test/java/com/booking/BookingTestPayload.json"));
            JSONObject jsonObject = (JSONObject)obj;
            Response response = bookingClient.postCallUsingPayload(Constants.Post_BookingCreateApi, jsonObject.toJSONString());
            Assert.assertEquals(response.getStatusCode(),200);
            Assert.assertNotNull(response.jsonPath().get("bookingid"));
            Assert.assertEquals(response.jsonPath().get("booking.firstname"),jsonObject.get("firstname"));
            Assert.assertEquals(response.jsonPath().get("booking.lastname"),jsonObject.get("lastname"));
            Assert.assertEquals(response.jsonPath().get("booking.totalprice").toString(),jsonObject.get("totalprice").toString());
            Assert.assertEquals(response.jsonPath().get("booking.depositpaid"),jsonObject.get("depositpaid"));
            Assert.assertEquals(response.jsonPath().get("booking.bookingdates"),jsonObject.get("bookingdates"));
            Assert.assertEquals(response.jsonPath().get("booking.additionalneeds"),jsonObject.get("additionalneeds"));
        }

    @Test(priority = 2, description = "Verify the Get API to check it contains recent booking id")
    public void verifyTheGetApiToCheckItContainsBookingId() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/test/java/com/booking/BookingTestPayload.json"));
        JSONObject jsonObject = (JSONObject)obj;
        Response response = bookingClient.postCallUsingPayload(Constants.Post_BookingCreateApi, jsonObject.toJSONString());
        Assert.assertEquals(response.getStatusCode(),200);
        int bookingId = response.jsonPath().get("bookingid");

        Response getBookingIds = bookingClient.getCall(Constants.Get_BookingIds);
        JSONArray listOfAllIds = new JSONArray();
        listOfAllIds.addAll(getBookingIds.jsonPath().get("bookingid"));
        Assert.assertEquals(getBookingIds.getStatusCode(),200);
        Assert.assertEquals(listOfAllIds.contains(bookingId),true);
    }

    @Test(priority = 3, description = "Verify the Get API using By ID to check it have correct details")
    public void verifyTheGetApiByIDToCheckItHaveCorrectBookingDetails() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/test/java/com/booking/BookingTestPayload.json"));
        JSONObject jsonObject = (JSONObject)obj;
        Response response = bookingClient.postCallUsingPayload(Constants.Post_BookingCreateApi, jsonObject.toJSONString());
        Assert.assertEquals(response.getStatusCode(),200);
        int bookingId = response.jsonPath().get("bookingid");

        Response getBookingDetails = bookingClient.getCall(Constants.Get_BookingById + bookingId);
        Assert.assertEquals(getBookingDetails.getStatusCode(),200);
        Assert.assertEquals(getBookingDetails.jsonPath().get("firstname"),jsonObject.get("firstname"));
        Assert.assertEquals(getBookingDetails.jsonPath().get("lastname"),jsonObject.get("lastname"));
        Assert.assertEquals(getBookingDetails.jsonPath().get("totalprice").toString(),jsonObject.get("totalprice").toString());
        Assert.assertEquals(getBookingDetails.jsonPath().get("depositpaid"),jsonObject.get("depositpaid"));
        Assert.assertEquals(getBookingDetails.jsonPath().get("bookingdates"),jsonObject.get("bookingdates"));
        Assert.assertEquals(getBookingDetails.jsonPath().get("additionalneeds"),jsonObject.get("additionalneeds"));
    }

    @Test(priority = 4, description = "Verify the Patch API to update the created booking data")
    public void verifyThePatchApiByIdToUpdateTheCreatedBookingData() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/test/java/com/booking/BookingTestPayload.json"));
        JSONObject jsonObject = (JSONObject)obj;
        Response response = bookingClient.postCallUsingPayload(Constants.Post_BookingCreateApi, jsonObject.toJSONString());
        Assert.assertEquals(response.getStatusCode(),200);
        int bookingId = response.jsonPath().get("bookingid");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("firstname","Test1");
        Response patchUpdatedDetails = bookingClient.patchCall(Constants.Patch_PartialUpdateBooking + bookingId,"token=abc123",jsonObject1.toJSONString());
        Assert.assertEquals(patchUpdatedDetails.getStatusCode(),200);
        Assert.assertEquals(patchUpdatedDetails.jsonPath().get("firstname"),"Test1");
    }

    @Test(priority = 5, description = "Verify the Delete API to delete the booking Id")
    public void verifyTheDeleteApiByIdToDeleteTheBookingId() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/test/java/com/booking/BookingTestPayload.json"));
        JSONObject jsonObject = (JSONObject)obj;
        Response response = bookingClient.postCallUsingPayload(Constants.Post_BookingCreateApi, jsonObject.toJSONString());
        Assert.assertEquals(response.getStatusCode(),200);
        int bookingId = response.jsonPath().get("bookingid");

        Response patchUpdatedDetails = bookingClient.deleteCall(Constants.Delete_Booking + bookingId,"token=abc123");
        Assert.assertEquals(patchUpdatedDetails.getStatusCode(),200);

        //get the booking id - expected not found
        Response getBookingDetails = bookingClient.getCall(Constants.Get_BookingById + bookingId);
        Assert.assertEquals(getBookingDetails.getStatusCode(),404);
    }

}

