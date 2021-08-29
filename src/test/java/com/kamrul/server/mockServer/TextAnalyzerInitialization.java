package com.kamrul.server.mockServer;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.initialize.ExpectationInitializer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

public class TextAnalyzerInitialization implements ExpectationInitializer {
    @Override
    public void initializeExpectations(MockServerClient mockServerClient) {
        mockServerClient
                .when(
                        HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/api/detectLanguage/")
                )
                .respond(
                        HttpResponse.response()
                                .withHeader(new Header("Content-Type","application/json"))
                                .withBody("{\"bangla\":0.03,\"banglish\":0.0,\"english\":0.97}")
                );
        mockServerClient
                .when(
                        HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/api/isPoliticalPost/")
                )
                .respond(
                        HttpResponse.response()
                                .withHeader(new Header("Content-Type","application/json"))
                                .withBody("{\"languageProbability\":{\"bangla\":0.3746078226312486,\"banglish\":0.3781635641079272,\"english\":0.24722861326082424},\"probabilityOfBeingPolitical\":0.0,\"banglaProbability\":0.9840937070644661,\"banglishProbability\":0.9840937070644661,\"englishProbability\":0.007919285467609505}")
        );
    }
}
