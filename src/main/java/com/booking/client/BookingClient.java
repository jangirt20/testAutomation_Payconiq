package com.booking.client;

import io.restassured.response.Response;

public class BookingClient {

    public Response postCallUsingPayload(String url, String payload)
    {
        Response response = RestClient.postCall(url,payload);
        return response;
    }

    public Response getCall(String url)
    {
        Response response = RestClient.getCall(url);
        return response;
    }

    public Response patchCall(String url,String auth,String payload)
    {
        Response response = RestClient.patchApiCall(url,auth,payload);
        return response;
    }

    public Response deleteCall(String url,String auth)
    {
        Response response = RestClient.deleteApiCall(url,auth);
        return response;
    }

}
