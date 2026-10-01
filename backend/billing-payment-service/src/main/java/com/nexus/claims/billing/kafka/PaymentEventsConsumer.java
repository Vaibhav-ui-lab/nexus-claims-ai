package com.nexus.claims.billing.kafka;

import com.nexus.claims.contracts.events.ClaimAdjudicatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventsConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventsConsumer.class);

    @KafkaListener(
        topics = "claim.saga.adjudicated.v1",
        groupId = "billing-payment-group"
    )
    public void onClaimAdjudicated(ClaimAdjudicatedEvent event) {
        log.info("Billing Payment Service received Adjudicated Claim event: {}", event.claimId());
        if ("AUTO_APPROVED".equals(event.finalStatus().name()) || "MANUALLY_APPROVED".equals(event.finalStatus().name())) {
            log.info("Disbursing ACH payout of ${} to provider {}", event.amountApproved(), event.providerId());
        } else {
            log.info("Claim {} status is {}. Payment withheld.", event.claimId(), event.finalStatus());
        }
    }
}
