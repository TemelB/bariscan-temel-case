package com.casestudy.hooks;

import com.casestudy.context.PetContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public final class ApiHooks {
    private final PetContext context;

    public ApiHooks(PetContext context) {
        this.context = context;
    }

    @After(order = 20)
    public void cleanUpOwnedPets() {
        for (long id : context.ownedIds) {
            context.client.delete(id).then().statusCode(anyOf(is(200), is(404)));
        }
    }

    @After(order = 10)
    public void printScenarioTraffic(Scenario scenario) {
        if (context.client == null) {
            return;
        }
        String traffic = context.client.trafficLog();
        if (traffic.isBlank()) {
            return;
        }
        System.out.println("\nAPI REQUEST / RESPONSE — " + scenario.getName());
        System.out.println(traffic);
        scenario.attach(traffic, "text/plain", "API request and response log");
    }
}
