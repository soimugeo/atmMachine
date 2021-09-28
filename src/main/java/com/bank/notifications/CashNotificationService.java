package com.bank.notifications;

import com.bank.cash.CashDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CashNotificationService {

    private CashNotificationLimits cashNotificationLimits;


    @Autowired
    public CashNotificationService(CashNotificationLimits cashNotificationLimits){
        this.cashNotificationLimits = cashNotificationLimits;
    }


    public List<String> notifyBillNumberDrop(List<CashDTO> cashDTOList){
        List<String> notificationList = new ArrayList<>();
        cashDTOList.forEach(cashDTO -> {
            long withdrawNumberOfBills = cashDTO.getNumberOfAvailableBills();
            int cashBillValue = cashDTO.getBillValue();

            switch (cashBillValue){
                case 100 -> {
                    if (withdrawNumberOfBills<cashNotificationLimits.getHundredUnderTen()){
                        String message = MessageFormat.format("CRITICAL ----> EMAIL NOTIFICATION: {0} \nCash bill numbers for {1} have dropped under 10%!", cashNotificationLimits.getAlertPhoneNumber(), cashBillValue);
                        notificationList.add(message);
                    }else if (withdrawNumberOfBills<cashNotificationLimits.getHundredUnderTwenty()){
                        String message = MessageFormat.format("WARNING ----> SMS NOTIFICATION nr:{0} \nCash bill numbers for {1} have dropped under 20%!",cashNotificationLimits.getAlertPhoneNumber(), cashBillValue);
                        notificationList.add(message);
                    }
                }
                case 50 -> {
                    if (withdrawNumberOfBills<cashNotificationLimits.getFiftyUnderFifteen()){
                        String message = MessageFormat.format("WARNING ----> EMAIL NOTIFICATION:{0} \nCash bill numbers for {1} have dropped under 15%!",cashNotificationLimits.getAlertEmailAddress(), cashBillValue);
                        notificationList.add(message);

                    }
                }
            }
        });

        return notificationList;
    }
}
