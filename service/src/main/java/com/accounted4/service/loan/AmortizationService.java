package com.accounted4.service.loan;


import com.accounted4.money.Money;
import com.accounted4.money.loan.AmortizationAttributes;
import com.accounted4.money.loan.AmortizationCalculator;
import com.accounted4.money.loan.ScheduledPayment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;


/**
 * Amortization calculation utilities.
 * 
 * @author Glenn Heinze
 */
@Service
public class AmortizationService {
    
    /**
     * Retrieve a list of scheduled payments.
     * 
     * @param amAttrs
     * @return 
     */
    public List<ScheduledPayment> getAmortizationSchedule(final AmortizationAttributes amAttrs) {

        List<ScheduledPayment> paymentList = new ArrayList<>();

        Iterator<ScheduledPayment> payments = AmortizationCalculator.getPayments(amAttrs);
        while (payments.hasNext()) {
            paymentList.add(payments.next());
        }

        return paymentList;

    }
    


    /**
     * The periodic payment required to fulfill request attributes.
     * 
     * @param amAttrs
     * @return 
     */
    public Money getMonthlyPayment(final AmortizationAttributes amAttrs) {
        return AmortizationCalculator.getMonthlyPayment(amAttrs);
    }

    
}
