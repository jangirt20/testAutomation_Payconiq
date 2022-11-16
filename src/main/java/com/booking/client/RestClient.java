package com.booking.client;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestClient {


    //createBooking
    public static Response postCall(final String endpoint, final String payload)
    {
        return given().contentType(ContentType.JSON)
                .body(payload).log().everything().expect().log().ifError()
                .when().post(endpoint);
    }

    // getBookingById
    public static Response getCall(final String endpoint)
    {
        return given().contentType(ContentType.JSON)
                .when().get(endpoint);
    }

    // PartialUpdateBooking
    public static Response patchApiCall(final String endpoint, final String authorization, final String payload)
    {
        return given().contentType(ContentType.JSON)
                .header( new Header("Cookie",authorization))
                .body(payload).log().everything().expect().log().ifError()
                .when().patch(endpoint);
    }

    //deleteBooking
    public static Response deleteApiCall(final String endpoint, final String authorization)
    {
        return given().contentType(ContentType.JSON)
                .header( new Header("Cookie",authorization)).log().everything().expect().log().ifError()
                .when().delete(endpoint);
    }

    //getBookingIds
    public static Response getCall1(final String endpoint, final Map<String,String> params)
    {
        return given().contentType(ContentType.JSON)
                .queryParams(params).log().everything()
                .get(endpoint);
    }
}
