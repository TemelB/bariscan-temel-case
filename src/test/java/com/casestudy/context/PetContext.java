package com.casestudy.context;

import com.casestudy.clients.PetClient;
import io.restassured.response.Response;
import java.util.HashSet;
import java.util.Set;

/** Mutable API state is scoped to a single Cucumber scenario. */
public final class PetContext {
    public PetClient client;
    public final Set<Long> ownedIds = new HashSet<>();
    private long petId;
    public Response response;

    public long getPetId() {
        if (petId == 0) {
            throw new IllegalStateException("Allocate an unused pet ID or prepare a pet fixture first");
        }
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }
}
