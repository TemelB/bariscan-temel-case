package com.casestudy.fixtures;

import com.casestudy.context.PetContext;
import java.util.concurrent.ThreadLocalRandom;
import static org.hamcrest.Matchers.equalTo;

/** Prepares independent test data without hiding setup logic in scenario steps. */
public final class PetFixture {
    private static final int MAX_ID_ATTEMPTS = 3;
    private static final long MIN_PET_ID = 1_000_000_000L;
    private static final long MAX_PET_ID = 8_001_000_000_000L;
    private final PetContext context;

    public PetFixture(PetContext context) {
        this.context = context;
    }

    public long findUnusedId() {
        for (int attempt = 0; attempt < MAX_ID_ATTEMPTS; attempt++) {
            long id = ThreadLocalRandom.current().nextLong(MIN_PET_ID, MAX_PET_ID);
            var response = context.client.get(id);
            if (response.statusCode() == 404) {
                return id;
            }
            // Retry only an ID collision. Infrastructure errors must fail immediately.
            response.then().statusCode(200);
        }
        throw new IllegalStateException("No unused pet ID found after " + MAX_ID_ATTEMPTS + " attempts");
    }

    public void createExistingPet(String name, String status) {
        context.setPetId(findUnusedId());
        long id = context.getPetId();
        context.ownedIds.add(id);
        context.client.create(id, name, status).then()
                .statusCode(200)
                .body("id", equalTo(id), "name", equalTo(name), "status", equalTo(status));
        context.response = context.client.get(id);
        context.response.then()
                .statusCode(200)
                .body("id", equalTo(id), "name", equalTo(name), "status", equalTo(status));
    }
}
