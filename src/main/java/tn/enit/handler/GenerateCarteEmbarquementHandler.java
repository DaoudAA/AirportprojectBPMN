package tn.enit.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class GenerateCarteEmbarquementHandler implements JobHandler {

    @Override
    public void handle(JobClient client, ActivatedJob job) throws Exception {

        //System.out.println("*AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        final Map<String, Object> inputVariables = job.getVariablesAsMap();

        System.out.println("Variables Map: " + inputVariables);

        final String name = (String) inputVariables.get("textfield_name");
        //System.out.println(name+ "-----------------------");
        final String numeroPasseport = (String) inputVariables.get("textfield_Passport");
        //System.out.println(numeroPasseport+ "-----------------------");

       System.out.println("Données enregistrées du voyageur " + name + " ayant le passeport numéro " + numeroPasseport);

       client.newCompleteCommand(job.getKey()).send().join();
    }
}
