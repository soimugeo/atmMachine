package com.bank.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.cash-notification-limits")
public class CashNotificationLimits {
    private int hundredUnderTwenty;
    private int hundredUnderTen;
    private int fiftyUnderFifteen;
    private String alertPhoneNumber;
    private String alertEmailAddress;
}
