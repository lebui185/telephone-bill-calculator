package telephonebillcalculator;

import java.math.BigDecimal;

public class Program {

    public static void main(String[] args) {
        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculatorImpl();
        
        
        BigDecimal result = telephoneBillCalculator.calculate(""
            + "123,01-01-2020 07:50:00,01-01-2020 08:02:01\n"
            + "456,01-01-2020 08:00:00,01-01-2020 08:02:01\n"
            + "456,01-01-2020 08:00:00,01-01-2020 08:02:01"
        );
        
        System.out.println(result); 
    }

}
