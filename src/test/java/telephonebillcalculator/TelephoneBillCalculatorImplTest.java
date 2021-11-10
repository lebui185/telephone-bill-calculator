package telephonebillcalculator;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class TelephoneBillCalculatorImplTest {
    
    private TelephoneBillCalculatorImpl telephoneBillCalculatorImpl;
    
    @Before
    public void before() {
        this.telephoneBillCalculatorImpl = new TelephoneBillCalculatorImpl(); 
    }
    
    @Test
    public void calculate_rate1() {
        BigDecimal result = this.telephoneBillCalculatorImpl.calculate(""
                + "123,01-01-2020 08:00:00,01-01-2020 08:02:01\n"
                + "456,01-01-2020 08:00:00,01-01-2020 08:02:01\n"
                + "456,01-01-2020 08:00:00,01-01-2020 08:02:01"
        );
        
        assertTrue(result.compareTo(new BigDecimal(3)) == 0);
    }
    
    @Test
    public void calculate_rate1_and_rate2() {
        BigDecimal result = this.telephoneBillCalculatorImpl.calculate(""
            + "123,01-01-2020 07:59:00,01-01-2020 08:02:01\n"
            + "456,01-01-2020 08:00:00,01-01-2020 08:02:01\n"
            + "456,01-01-2020 08:00:00,01-01-2020 08:02:01"
        );
        
        assertTrue(result.compareTo(new BigDecimal(3.5)) == 0);
    }
    
    @Test
    public void calculate_rate1_rate2_rate3() {
        BigDecimal result = this.telephoneBillCalculatorImpl.calculate(""
            + "123,01-01-2020 07:50:00,01-01-2020 08:02:01\n"
            + "456,01-01-2020 08:00:00,01-01-2020 08:02:01\n"
            + "456,01-01-2020 08:00:00,01-01-2020 08:02:01"
        );
        System.out.println(result);
        assertTrue(result.compareTo(new BigDecimal(9.0)) == 0);
    }
    
}
