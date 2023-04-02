package de.viada.hackfest;

import java.util.UUID;

/** We assume every sensor is configured to send specific metadata to identify the product **/
public record SensorEvent(
        UUID marketId,
        UUID productId,
        String productName,
        ProductCategory productCategory,
        double sensorFillData) {

}
