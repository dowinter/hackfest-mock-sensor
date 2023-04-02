package de.viada.hackfest;

import java.util.UUID;

public record Product(UUID id, ProductCategory category, String name) {
}
