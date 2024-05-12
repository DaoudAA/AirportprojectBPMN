package tn.enit.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class RegisterTravelerDataHandler implements JobHandler {

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {
        // Retrieve variables from the job
        final Map<String, Object> inputVariables = job.getVariablesAsMap();

        // Extract required data from the variables
        final String travelerName = (String) inputVariables.get("textfield_NomComplet");
        final String passportNumber = (String) inputVariables.get("textfield_NumeroPasseport");
        final String ticketNumber = (String) inputVariables.get("textfield_NumeroBillet");
        final String cinNumber = (String) inputVariables.get("textfield_CIN");

        // Perform data registration logic
        // For demonstration purposes, we'll simply print the data
        System.out.println("Enregister les données importantes du voyageur:");
        System.out.println("Nom: " + travelerName);
        System.out.println("Numero de passport: " + passportNumber);
        System.out.println("Numero du billet: " + ticketNumber);
        System.out.println("Numero de CIN: " + cinNumber);

        // Complete the job
        client.newCompleteCommand(job.getKey()).send().join();
    }
}
