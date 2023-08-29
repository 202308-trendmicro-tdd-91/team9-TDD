package budget;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetService {

    private final BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public double query(LocalDate startDate, LocalDate endDate) {

        Map<String, Integer> yearMonthMap = getDatesBetween(startDate, endDate);
        List<Budget> budgets = budgetRepo.getAll();
        Map<String, Double> budgetMap = budgets.stream().collect(Collectors.toMap(
                Budget::getYearMonth,
                budget -> (double) budget.getAmount() / YearMonth.parse(budget.getYearMonth(), DateTimeFormatter.ofPattern("yyyyMM")).lengthOfMonth()
        ));

        double totalAmount = 0;
        for (String key : yearMonthMap.keySet()) {
            int days = yearMonthMap.get(key);
            totalAmount += budgetMap.getOrDefault(key, 0.0) * days;
        }

        return totalAmount;
    }

    public static Map<String, Integer> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> resultMap = new HashMap<>();
        LocalDate currentDate = startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        while (!currentDate.isAfter(endDate)) {
            String yearMonthString = currentDate.format(formatter);
            int currentYearMonthCount = resultMap.getOrDefault(yearMonthString, 0);

            resultMap.put(yearMonthString, currentYearMonthCount + 1);
            currentDate = currentDate.plusDays(1);
        }


        return resultMap;
    }
}
