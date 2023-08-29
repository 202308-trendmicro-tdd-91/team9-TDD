package budget;

import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BudgetServiceTest {

    private final BudgetRepo budgetRepo = Mockito.mock(BudgetRepo.class);

    private final BudgetService budgetService = new BudgetService(budgetRepo);

    @Test
    public void test_1_day_budget() {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(new Budget("202301", 3100), new Budget("202302", 5600)));
        double actual = budgetService.query(LocalDate.parse("20230101", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalDate.parse("20230102", DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(actual).isEqualTo(200);
    }

    @Test
    public void test_cross_month_budget() {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(new Budget("202301", 3100), new Budget("202302", 5600)));
        double actual = budgetService.query(LocalDate.parse("20230131", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalDate.parse("20230201", DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(actual).isEqualTo(300);
    }

    @Test
    public void test_cross_year_budget() {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(new Budget("202212", 3100), new Budget("202301", 6200)));
        double actual = budgetService.query(LocalDate.parse("20221230", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalDate.parse("20230102", DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(actual).isEqualTo(600);
    }

    @Test
    public void test_invalid_period() {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(new Budget("202212", 3100), new Budget("202301", 6200)));
        double actual = budgetService.query(LocalDate.parse("20221230", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalDate.parse("20221228", DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(actual).isZero();
    }

    @Test
    public void test_no_data() {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(new Budget("202212", 3100), new Budget("202301", 6200)));
        double actual = budgetService.query(LocalDate.parse("20220401", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalDate.parse("20220405", DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void test_leap_year() {
        when(budgetRepo.getAll()).thenReturn(Arrays.asList(new Budget("202002", 2900), new Budget("202003", 6200)));
        double actual = budgetService.query(LocalDate.parse("20200228", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalDate.parse("20200301", DateTimeFormatter.ofPattern("yyyyMMdd")));
        assertThat(actual).isEqualTo(400);
    }
}
