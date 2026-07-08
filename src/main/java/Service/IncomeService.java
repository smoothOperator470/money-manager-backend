package Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import Repository.CategoryRepository;
import Repository.IncomeRepository;
import dto.IncomeDTO;
import lombok.RequiredArgsConstructor;
import Entity.ProfileEntity;
import Entity.CategoryEntity;
import Entity.IncomeEntity;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.domain.Sort;
@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository; 
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    //add an expense

    public IncomeDTO addIncome(IncomeDTO incomeDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository.findById(incomeDTO.getCategoryId()).orElseThrow(()-> new RuntimeException("Category not found"));

        IncomeEntity newExpense = toEntity(incomeDTO,profile,categoryEntity);
        incomeRepository.save(newExpense);
        return toDTO(newExpense);


        
        

        
    }

    //get all expense for a profile based on start and end date

    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);

        return list.stream().map(this::toDTO).toList();
    }

    private IncomeDTO toDTO(IncomeEntity incomeEntity){
        return IncomeDTO.builder()
        .id(incomeEntity.getId())
        .name(incomeEntity.getName())
        .icon(incomeEntity.getIcon())
        .date(incomeEntity.getDate())
        .amount(incomeEntity.getAmount())
        .createdAt(incomeEntity.getCreatedAt())
        .updatedAt(incomeEntity.getUpdatedAt())
        .categoryName(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getName():null)
        .categoryId(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getId():null)
        .build();
    }

    private IncomeEntity toEntity(IncomeDTO incomeDTO,ProfileEntity profileEntity,CategoryEntity categoryEntity){
        return IncomeEntity.builder()
        .id(incomeDTO.getId())
        .name(incomeDTO.getName())
        .icon(incomeDTO.getIcon())
        .date(incomeDTO.getDate())
        .amount(incomeDTO.getAmount())
        .createdAt(incomeDTO.getCreatedAt())
        .updatedAt(incomeDTO.getUpdatedAt())
        .category(categoryEntity)
        .profile(profileEntity)
        .build();
    }

    //delete income by id for current user
    public void deleteIncome(Long incomeId){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        IncomeEntity incomeEntity = incomeRepository.findById(incomeId).orElseThrow(()->new RuntimeException("Income Not Found"));
        //is the current logged in user the owner of the income
        if(!incomeEntity.getProfile().getId().equals(profileEntity.getId())){
            throw new RuntimeException("You are not authorized to delete this income");
        }
        incomeRepository.delete(incomeEntity);

    }


    //get latest five incomes for the current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profileEntity.getId());
        return list.stream().map(this::toDTO).toList();
    }

    //get total income for current user
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        BigDecimal totalIncome = incomeRepository.findTotalIncomeByProfileId(profileEntity.getId());
        return totalIncome != null ? totalIncome : BigDecimal.ZERO;

    }
    //filter income
     public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }
}