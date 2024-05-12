package tn.enit;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import tn.enit.handler.RegisterTravelerDataHandler;

import java.time.Duration;
import java.util.Scanner;

public class TravelerDataRegistrationMain {
    private static final String ZEEBE_ADDRESS = "0d799340-396e-4e01-9844-2f1ddf87db80.ont-1.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "qfiXcDhqFE0zNGb_3~kjctn.AUxnFvCs";
    private static final String ZEEBE_CLIENT_SECRET = "rM1s8rK2bAsw-Fwh2OdyztEIYhR_42QbHrg4ClHIIle_Z5GHuxkXUC6K~uXNV5pj";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";

    public static void main(String[] args) {
        runTravelerDataRegistration();
    }

    private static void runTravelerDataRegistration() {
        final OAuthCredentialsProvider credentialsProvider =
                new OAuthCredentialsProviderBuilder()
                        .authorizationServerUrl(ZEEBE_AUTHORIZATION_SERVER_URL)
                        .audience(ZEEBE_TOKEN_AUDIENCE)
                        .clientId(ZEEBE_CLIENT_ID)
                        .clientSecret(ZEEBE_CLIENT_SECRET)
                        .build();

        try (final ZeebeClient client =
                     ZeebeClient.newClientBuilder()
                             .gatewayAddress(ZEEBE_ADDRESS)
                             .credentialsProvider(credentialsProvider)
                             .build()) {
            System.out.println("Connected to: " + client.newTopologyRequest().send().join());

            final JobWorker registerTravelerDataWorker =
                    client.newWorker()
                            .jobType("TravelerDataRegistration")
                            .handler(new RegisterTravelerDataHandler())
                            .timeout(Duration.ofSeconds(10).toMillis())
                            .name("Enregister les données importantes liées au voyageur")
                            .open();

            // This thread is kept alive to prevent the program from terminating immediately
            Thread.sleep(10000);
            Scanner sc = new Scanner(System.in);
            sc.nextInt();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}