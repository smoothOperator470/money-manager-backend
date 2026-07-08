package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDTO {
     private Long id;
    private String name;
    private String icon;
    private String categoryName;
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //the DTO dictates exactly what JSON is sent or recieved from the client and not the entity and entity is for the database,hibernate maps the entity to the database tables.
    //Why not sent the entire CategoryEntity object in the JSON?

    //1.Security: The CategoryEntity might contain sensitive information like profile_id which we don't want to expose to the client.



    //2.Performance: The CategoryEntity might be a large object and sending it in the JSON would increase the payload size.



    //3.Preventing "Infinite Recursion": If IncomeEntity contains CategoryEntity, and CategoryEntity happened to contain a List of IncomeEntities... when you try to convert that to JSON, it will bounce back and forth infinitely until your app crashes (a very common error called StackOverflowError
   

}
