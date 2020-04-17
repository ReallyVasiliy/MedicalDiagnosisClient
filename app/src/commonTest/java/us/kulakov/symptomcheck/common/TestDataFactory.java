package us.kulakov.symptomcheck.common;

import java.util.Random;

import us.kulakov.symptomcheck.data.model.response.endless.InitSessionResponse;

/**
 * Factory class that makes instances of data models with random field values. The aim of this class
 * is to help setting up test fixtures.
 */
public class TestDataFactory {

    private static final Random random = new Random();

    public static InitSessionResponse makeInitSessionResponse(boolean success) {
        InitSessionResponse r = new InitSessionResponse();
        r.status = success ? "ok" : "error";
        r.sessionID = "mock_session_id";
        return r;
    }
}
