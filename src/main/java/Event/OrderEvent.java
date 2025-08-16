package Event;

import java.util.UUID;

public record OrderEvent(
        UUID orderId,
        String customerEmail,
        long amountCents,
        String status
) {}