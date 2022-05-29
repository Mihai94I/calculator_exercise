package digital.metro.pricing.calculator.dto;

import java.util.*;

public final class Basket {

    private final String customerId;
    private final Set<BasketEntry> entries;

    public Basket(String customerId, Set<BasketEntry> entries) {
        this.customerId = customerId;
        this.entries = new HashSet<>(entries);
    }

    public String getCustomerId() {
        return customerId;
    }

    public Set<BasketEntry> getEntries() {
        return new HashSet<>(entries);
    }
}
