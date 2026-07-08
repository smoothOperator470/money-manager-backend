package Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import Entity.ExpenseEntity;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.domain.Sort;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    //select from tbl_expenses where profile_id=? order by date desc
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    @Query(value = "select * from tbl_expenses where profileId = :profileId order by data desc limit 5",nativeQuery = true)
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(@Param("profileId") Long profileId);

    @Query("select sum(e.amount) from ExpenseEntity e where e.profile.id=:profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId")Long profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate,
        String keyword,
        Sort sort);

    List<ExpenseEntity> findByProfileIdAndDateBetween(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate
        );

    List<ExpenseEntity> findByProfileIdAndDate(Long profileId,LocalDate date);

}
