package com.bank.cash;

import com.bank.notifications.CashNotificationLimits;
import com.bank.notifications.CashNotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CashNotificationServiceTest {

    @Mock
    private CashNotificationLimits cashNotificationLimits;

    private CashNotificationService cashNotificationService;

    private List<CashDTO> mockedDbCashList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cashNotificationService = new CashNotificationService(cashNotificationLimits);


        Mockito.when(cashNotificationLimits.getFiftyUnderFifteen()).thenReturn(8);
        Mockito.when(cashNotificationLimits.getHundredUnderTen()).thenReturn(5);
        Mockito.when(cashNotificationLimits.getHundredUnderTwenty()).thenReturn(10);
        Mockito.when(cashNotificationLimits.getAlertPhoneNumber()).thenReturn("someNumber");
        Mockito.when(cashNotificationLimits.getAlertEmailAddress()).thenReturn("some@email.com");


        mockedDbCashList = new ArrayList<>();
        mockedDbCashList.add(CashDTO.builder()
                .billValue(100)
                .numberOfAvailableBills(10)
                .build());

        mockedDbCashList.add(CashDTO.builder()
                .billValue(50)
                .numberOfAvailableBills(10)
                .build());
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void notifyBillNumberDrop() {

        Assertions.assertTrue(cashNotificationService.notifyBillNumberDrop(mockedDbCashList).isEmpty());


        List<CashDTO> cashMockedWithdrawList = new ArrayList<>();
        cashMockedWithdrawList.add(CashDTO.builder()
                .billValue(100)
                .numberOfAvailableBills(3)
                .build());
        List<String> notificationMessageList = cashNotificationService.notifyBillNumberDrop(cashMockedWithdrawList);
        Assertions.assertFalse(notificationMessageList.isEmpty());
        Assertions.assertTrue(notificationMessageList.stream().anyMatch(message -> message.contains("EMAIL NOTIFICATION")));


        cashMockedWithdrawList.clear();
        cashMockedWithdrawList.add(CashDTO.builder()
                .billValue(100)
                .numberOfAvailableBills(6)
                .build());
        notificationMessageList = cashNotificationService.notifyBillNumberDrop(cashMockedWithdrawList);
        Assertions.assertTrue(notificationMessageList.stream().anyMatch(message -> message.contains("SMS NOTIFICATION")));


        cashMockedWithdrawList.clear();
        cashMockedWithdrawList.add(CashDTO.builder()
                .billValue(100)
                .numberOfAvailableBills(11)
                .build());
        Assertions.assertTrue(cashNotificationService.notifyBillNumberDrop(cashMockedWithdrawList).isEmpty());


        cashMockedWithdrawList.clear();
        cashMockedWithdrawList.add(CashDTO.builder()
                .billValue(50)
                .numberOfAvailableBills(2)
                .build());
        notificationMessageList = cashNotificationService.notifyBillNumberDrop(cashMockedWithdrawList);
        Assertions.assertTrue(notificationMessageList.stream().anyMatch(message -> message.contains("WARNING")));


        cashMockedWithdrawList.clear();
        cashMockedWithdrawList.add(CashDTO.builder()
                .billValue(50)
                .numberOfAvailableBills(9)
                .build());
        notificationMessageList = cashNotificationService.notifyBillNumberDrop(cashMockedWithdrawList);
        Assertions.assertTrue(notificationMessageList.isEmpty());

    }
}