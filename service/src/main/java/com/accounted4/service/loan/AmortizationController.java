package com.accounted4.service.loan;

import com.accounted4.money.loan.AmortizationAttributes;
import com.accounted4.money.loan.ScheduledPayment;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handle Restful requests for /loan services.
 * 
 */
@RestController
public class AmortizationController {
    
    
    @Autowired private AmortizationService amortizationService;
    @Autowired private AmortizationServiceJasperReport amortizationServiceJasperReport;
    
    /**
     * Generate an amortization schedule returned as a json object.
     * 
     * Example invocation:
     * curl -i -H "Accept: application/json" -H "Content-Type: application/json" --data '{"loanAmount":"20000.00", "regularPayment":"200","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"12","interestOnly":"false","amortizationPeriodMonths":"120","compoundingPeriodsPerYear":"2","interestRate":"10"}' http://localhost:8080/loan/schedule.json
     * 
     * Note through cli: all fields required, no tabs/line breaks
     * Set: Content-Type: application/json
     * Content: {"loanAmount":"20000.00", "regularPayment":"200","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"12","interestOnly":"false","amortizationPeriodMonths":"20","compoundingPeriodsPerYear":"2","interestRate":"10"}
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
 
    
    /**
     * Stream back an amortization schedule in pdf format.
     * 
     * Example invocation:
     * curl -i -H "Accept: application/pdf" -H "Content-Type: application/json" --data '{"loanAmount":"20000.00", "regularPayment":"0","startDate":"2013-01-05","adjustmentDate":"2013-01-15","termInMonths":"12","interestOnly":"false","amortizationPeriodMonths":"120","compoundingPeriodsPerYear":"2","interestRate":"10"}' http://localhost:8080/loan/schedule.pdf -o junk.pdf
     * 
     * @param amAttrs
     * @param response
     * @throws IOException
     * @throws JRException 
     */
    @RequestMapping(
            value = "/loan/schedule.pdf"
            ,method = RequestMethod.POST
            ,produces = "application/pdf"
    )
    public void getSchedulePdf(@RequestBody AmortizationAttributes amAttrs, HttpServletResponse response)
            throws IOException, JRException {
        response.setContentType("application/pdf");
        amortizationServiceJasperReport.getAmortizationSchedulePdf(amAttrs, response.getOutputStream());
    }

    
}
