package telephonebillcalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {
    
    private static final LocalTime RATE_1_START_INTERVAL = LocalTime.of(8, 0);
    private static final LocalTime RATE_2_END_INTERVAL = LocalTime.of(16, 0);
    
    private static final BigDecimal RATE_1 = BigDecimal.ONE;
    private static final BigDecimal RATE_2 = new BigDecimal(0.5);
    private static final BigDecimal RATE_3 = new BigDecimal(0.2);
    
    public BigDecimal calculate(String phoneLogCsv) {
        List<PhoneLogItem> phoneLogItems = parseCsvPhoneLog(phoneLogCsv);
        
        String mostCalledNumber = findMostCalledNumber(phoneLogItems);
        
        BigDecimal total = BigDecimal.ZERO;
        for (PhoneLogItem logItem : phoneLogItems) {
            if (Objects.equals(mostCalledNumber, logItem.getPhoneNumber())) {
                continue;
            }
            
            BigDecimal current = calculatePhoneLogItemBill(logItem);
            total = total.add(current);
        }
        
        return total;
    }
    
    private BigDecimal calculatePhoneLogItemBill(PhoneLogItem logItem) {
        int rate1Count = 0;
        int rate2Count = 0;
        
        LocalDateTime currentTime = logItem.getStartTime();
        while (!currentTime.isAfter(logItem.getEndTime())) {
            if (isInsideInterval(currentTime)) {
                rate1Count++; 
            } else {
                rate2Count++;
            }
            
            currentTime = currentTime.plusMinutes(1);
        }
        
        int rate3Count = rate2Count > 5 ? (rate2Count - 5) : 0;
    
        System.out.println(rate1Count);
        System.out.println(rate2Count);
        System.out.println(rate3Count);
        
        BigDecimal rate1Cost = new BigDecimal(rate1Count).multiply(RATE_1);
        BigDecimal rate2Cost = new BigDecimal(rate2Count).multiply(RATE_2);
        BigDecimal rate3Cost = new BigDecimal(rate3Count).multiply(RATE_3);
        
        return rate1Cost.add(rate2Cost).add(rate3Cost);
    }
    
    private boolean isInsideInterval(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        
        LocalTime intervalStart = RATE_1_START_INTERVAL;
        LocalTime intervalEnd = RATE_2_END_INTERVAL;
        
        return !currentTime.isBefore(intervalStart)
            && !currentTime.isAfter(intervalEnd);
    }

    private List<PhoneLogItem> parseCsvPhoneLog(String phoneLogCsv) {
        String[] lines = phoneLogCsv.split("\n");
        
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy' 'HH:mm:ss");
        
        List<PhoneLogItem> items = new ArrayList<>();
        for (String line : lines) {
            String[] lineParts = line.split(",");
            
            PhoneLogItem item = new PhoneLogItem();
            
            item.setPhoneNumber(lineParts[0]);
            item.setStartTime(LocalDateTime.parse(lineParts[1], dateTimeFormatter));
            item.setEndTime(LocalDateTime.parse(lineParts[2], dateTimeFormatter));
            
            items.add(item);
        }
        
        return items;
    }

    private static class PhoneLogItem {
        private String phoneNumber;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public LocalDateTime getStartTime() {
            return startTime;
        }
        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }
        public LocalDateTime getEndTime() {
            return endTime;
        }
        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
        
    }
    
    private String findMostCalledNumber(List<PhoneLogItem> logs) {
        if (logs == null || logs.isEmpty()) {
            return null;
        }
        
        Map<String, Long> phoneInfos = countByPhones(logs);
        LinkedHashMap<String, Long> sortedPhoneInfos = sortByValue(phoneInfos);
        
        long maxValue = -1;
        String phone = null;
        for (Entry<String, Long> entry : sortedPhoneInfos.entrySet()) {
            if (maxValue == -1) {
                maxValue = entry.getValue();
                phone = entry.getKey();
                break;
            }
            
            if (entry.getValue() < maxValue) {
                break;
            }
            
            if (Long.parseLong(entry.getKey()) > Long.parseLong(phone)) {
                phone = entry.getKey();
            }
        }
        
        return phone;
    }
    

    private static Map<String, Long> countByPhones(List<PhoneLogItem> logs) {
        return logs.stream()
                .filter(Objects::nonNull)
                .map(PhoneLogItem::getPhoneNumber)
                .collect(Collectors.groupingBy(phone -> phone, Collectors.counting()));
    }
    
    private static LinkedHashMap<String, Long> sortByValue(final Map<String, Long> phoneInfos) {
        return phoneInfos.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Long>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


}
