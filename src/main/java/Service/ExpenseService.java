

package Service;
import Entity.CategoryEntity;
import Entity.ExpenseEntity;
import dto.ExpenseDTO;
import Entity.ProfileEntity;
import Service.ProfileService;
import Repository.ExpenseRepository;
import Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ProfileService profileService;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    // Add a new expense to the database
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        ExpenseEntity newExpense = toEntity(expenseDTO, profileEntity, categoryEntity);
        expenseRepository.save(newExpense);
        return toDTO(newExpense);

    }

    // Retreive all expense for the current user for current month
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profileEntity.getId(), startDate,
                endDate);
        return list.stream().map(this::toDTO).toList();

    }

    // delete expenses by id for current user

    public void deleteExpense(Long expenseId) {
        // whether this expense exists or not
        ExpenseEntity expenseEntity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        // check whether user is deleting his own expense or not
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        if (!profileEntity.getId().equals(expenseEntity.getProfile().getId())) {
            throw new RuntimeException("You are not authorized to delete this expense");
        }
        expenseRepository.delete(expenseEntity);

    }

    // get latest five expenses for the current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser() {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profileEntity.getId());
        return list.stream().map(this::toDTO).toList();
    }

    // get total expenses for the current year
    public BigDecimal getTotalExpensesForCurrentUser() {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profileEntity.getId());
        return total != null ? total : BigDecimal.ZERO;

    }

    // get expenses on a particular date

    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date) {
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate(profileId, date);
        return list.stream().map(this::toDTO).toList();

    }

    // filter expense

    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                profileEntity.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    // toEntity
    public ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return ExpenseEntity.builder()
                .id(expenseDTO.getId())
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .profile(profileEntity)
                .category(categoryEntity)
                .build();
    }

    // toDto
    public ExpenseDTO toDTO(ExpenseEntity expenseEntity) {
        return ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .amount(expenseEntity.getAmount())
                .date(expenseEntity.getDate())
                .categoryId(expenseEntity.getCategory().getId())
                .categoryName(expenseEntity.getCategory().getName())
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }

}