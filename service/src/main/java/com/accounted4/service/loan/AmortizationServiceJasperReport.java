package com.accounted4.service.loan;

import com.accounted4.money.Money;
import com.accounted4.money.loan.AmortizationAttributes;
import com.accounted4.money.loan.AmortizationCalculator;
import com.accounted4.money.loan.ScheduledPayment;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Amortization services dealing with Jasper Reports.
 * 
 * @author gheinze
 */
@Service
public class AmortizationServiceJasperReport {
    
    private static final int MONTHS_PER_YEAR = 12;

    
    @Autowired
    private AmortizationScheduleJasperReport amortizationScheduleJasperReport;

    @Autowired
    AmortizationService amService;
    
    
    /**
     * Stream back a pdf document for an amortization schedule.
     * @param amAttrs
     * @param outputStream
     * @throws JRException
     * @throws IOException 
     */
    
    public void getAmortizationSchedulePdf(
            final AmortizationAttributes amAttrs
            ,final OutputStream outputStream
    ) throws JRException, IOException {
        
        List<ScheduledPayment> payments = amService.getAmortizationSchedule(amAttrs);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(payments);

        // TODO: name, title, etc should be configurable parameters as well
        Map<String, Object> customParameters = new HashMap<>();
        customParameters.put("amount", amAttrs.getLoanAmount());
        customParameters.put("rate", amAttrs.getInterestRate());
        
        Money monthlyPayment = AmortizationCalculator.getMonthlyPayment(amAttrs);
        customParameters.put("monthlyPayment", monthlyPayment);
        customParameters.put("term", amAttrs.getTermInMonths());
        if (!amAttrs.isInterestOnly()) {
            customParameters.put("amortizationYears", amAttrs.getAmortizationPeriodMonths() / MONTHS_PER_YEAR);
            customParameters.put("amortizationMonths", amAttrs.getAmortizationPeriodMonths() % MONTHS_PER_YEAR);
            customParameters.put("compoundPeriod", amAttrs.getCompoundingPeriodsPerYear());
        }
        customParameters.put("mortgagee", "Accounted4");
        customParameters.put("mortgagor", "Accounted4");
        
        
        JasperReport compiledReport = amortizationScheduleJasperReport.getCompiledReport();

        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, customParameters, ds);

        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        
        //File pdfFile = File.createTempFile("amSchedule", ".pdf");
        //JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.getCanonicalPath());
        
    }

    
}
