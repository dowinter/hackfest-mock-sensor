package de.viada.hackfest;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class SensorDataGenerator {

    public static List<Product> MOCK_PRODUCTS = new ArrayList<>(
            List.of(
                    new Product(UUID.fromString("a73ef4a9-a92e-415e-bc06-aa17898bcdb4"), ProductCategory.DAIRY, "Milk"),
                    new Product(UUID.fromString("1df77758-8cbc-45f3-ba45-a1a3b9afa5f4"), ProductCategory.DAIRY, "Cheese"),
                    new Product(UUID.fromString("1e70e5a5-4c51-4c35-a96a-113c9149f3ab"), ProductCategory.DAIRY, "Cream Cheese"),
                    new Product(UUID.fromString("3c1eb59b-54ff-4796-8fc3-b34c2a4c64ab"), ProductCategory.DAIRY, "Yogurt"),
                    new Product(UUID.fromString("ae4867ef-dd20-465b-82fe-f7946d4cb717"), ProductCategory.DAIRY, "Butter"),
                    new Product(UUID.fromString("ec543b90-44c0-4468-815e-82bbcd5145e9"), ProductCategory.DAIRY, "Cream"),
                    new Product(UUID.fromString("a00fb2b3-c958-4444-9692-d0081bbba6a1"), ProductCategory.BAKED_GOODS, "Bread"),
                    new Product(UUID.fromString("3be14398-4df4-4e93-bad9-bbcd47cbdcb9"), ProductCategory.BAKED_GOODS, "Bread Roll"),
                    new Product(UUID.fromString("d42e2016-8b99-4615-8fd3-9ddbdd0f4c3f"), ProductCategory.BAKED_GOODS, "Cake"),
                    new Product(UUID.fromString("215b004f-79ea-4922-863e-328b33cf3d1d"), ProductCategory.BAKED_GOODS, "Apple Pie"),
                    new Product(UUID.fromString("21ce9a81-402f-4c1f-889c-d3b3b66c7c01"), ProductCategory.BAKED_GOODS, "Carrot Cake"),
                    new Product(UUID.fromString("44044110-e8b4-45ce-a643-9ca2095c5c88"), ProductCategory.VEGETABLES, "Tomato"),
                    new Product(UUID.fromString("c363c38c-c5e6-4a11-8467-47806ce23187"), ProductCategory.VEGETABLES, "Potato"),
                    new Product(UUID.fromString("ec3b4ae2-3097-4aa1-b07f-f1d60b3b2adc"), ProductCategory.VEGETABLES, "Carrot"),
                    new Product(UUID.fromString("110369f0-1b79-4e6b-be3a-430608756abd"), ProductCategory.VEGETABLES, "Cucumber"),
                    new Product(UUID.fromString("895dc71a-48de-4cef-8e22-ec1e50a4d68f"), ProductCategory.VEGETABLES, "Apple"),
                    new Product(UUID.fromString("e41e9f74-7548-40ea-aa74-952aa5c806e1"), ProductCategory.VEGETABLES, "Bell Pepper"),
                    new Product(UUID.fromString("197f7a52-0504-4a33-95f3-0acb81006f2c"), ProductCategory.VEGETABLES, "Cauliflower"),
                    new Product(UUID.fromString("a81a5d76-0e69-44f7-9aea-6316656833be"), ProductCategory.VEGETABLES, "Celery"),
                    new Product(UUID.fromString("468594f0-342e-4c46-90ff-297209105698"), ProductCategory.VEGETABLES, "Peas")
            ));

    @ConfigProperty(name = "market.id")
    UUID marketId;

    @ConfigProperty(name = "generator.data.items.count.min")
    Long min;

    @ConfigProperty(name = "generator.data.items.count.max")
    Long max;

    @Channel("sensor-event")
    Emitter<SensorEvent> emitter;

    @Scheduled(every = "${generator.data.interval:5s}")
    public void generateMockSensorData() {
        Collections.shuffle(MOCK_PRODUCTS);
        long count = Math.round((Math.random() * (max - min)) + min);
        MOCK_PRODUCTS.stream().limit(count)
                .map(p -> new SensorEvent(marketId, p.id(), p.name(), p.category(), Math.random()))
                .peek(e -> System.out.println("Sending = " + e)).forEach(e -> emitter.send(e));
    }
}
