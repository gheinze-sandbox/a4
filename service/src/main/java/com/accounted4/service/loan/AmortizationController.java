package com.accounted4.service.loan;

import com.accounted4.money.loan.AmortizationAttributes;
import com.accounted4.money.loan.ScheduledPayment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 
 */
@RestController
public class AmortizationController {
    
    
    @Autowired
    private AmortizationService amortizationService;
    
    
    /**
     * Generate an amortization schedule.
     * Note through cli: all fields required, no tabs/line breaks
     * Set: Content-Type: application/json
     * Content: {"loanAmount":"20000.00", "regularPayment":"200","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"12","interestOnly":"false","amortizationPeriodMonths":"20","compoundingPeriodsPerYear":"2","interestRate":"10"}
     * 
     * curl -i -H "Accept: application/json" -H "Content-Type: application/json" --data '{"loanAmount":"20000.00", "regularPayment":"200","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"120","interestOnly":"false","amortizationPeriodMonths":"20","compoundingPeriodsPerYear":"2","interestRate":"10"}' http://localhost:8080/loan/schedule.json
     * 
     * @param amAttrs
     * @return 
     */
    @RequestMapping(
            value = "/loan/schedule.json"
            ,method = RequestMethod.POST
            ,produces = "application/json"
    )
    public List<ScheduledPayment> getSchedule(@RequestBody AmortizationAttributes amAttrs) {
        return amortizationService.getAmortizationSchedule(amAttrs);
    }
 
    
    
}
