package tn.enit.handler;

import io. camunda. zeebe. client. ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

import java.util.HashMap;
import java.util.Map;

public class SendMessageHandler implements JobHandler {
    private static final String ZEEBE_ADDRESS = "0d799340-396e-4e01-9844-2f1ddf87db80.ont-1.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "qfiXcDhqFE0zNGb_3~kjctn.AUxnFvCs";
    private static final String ZEEBE_CLIENT_SECRET = "rM1s8rK2bAsw-Fwh2OdyztEIYhR_42QbHrg4ClHIIle_Z5GHuxkXUC6K~uXNV5pj";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";

    private static final String MESSAGE_NAME = "fetchCopieCarte";

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {

        final Map<String, Object> inputVariables = job.getVariablesAsMap();
        final String correlationKey = (String) inputVariables.get("CopiePassport");

        final String numPassport = (String) inputVariables.get("num_passeport");
        final String fullName = (String) inputVariables.get("textfield_name");
        final String flightText = (String) inputVariables.get("textfield_Vol");
        final String passportNumber = (String) inputVariables.get("textfield_Passport");
        final OAuthCredentialsProvider credentialsProvider =
                new OAuthCredentialsProviderBuilder()
                        .authorizationServerUrl(ZEEBE_AUTHORIZATION_SERVER_URL)
                        .audience(ZEEBE_TOKEN_AUDIENCE)
                        .clientId(ZEEBE_CLIENT_ID)
                        .clientSecret(ZEEBE_CLIENT_SECRET)
                        .build();
        try (final ZeebeClient agentCompagnieAerienne = ZeebeClient.newClientBuilder()
                .gatewayAddress(ZEEBE_ADDRESS)
                .credentialsProvider(credentialsProvider)
                .build()) {

        final Map<String, Object> messageVariables = new HashMap<>();
        messageVariables.put("num_passeport", numPassport);
        messageVariables.put("full_name", fullName);
        messageVariables.put("flight_text", flightText);
        messageVariables.put("passport_number", passportNumber);

            agentCompagnieAerienne.newPublishMessageCommand()
                .messageName(MESSAGE_NAME)
                .correlationKey(correlationKey)
                .variables(messageVariables)
                .send()
                .join();
            System.out.println(" Copie de carte envoyee au passager ayant passport num " + passportNumber );
            client.newCompleteCommand(job.getKey()).send().join();
    }
    }
}
