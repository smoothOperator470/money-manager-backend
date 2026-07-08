package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //Required by the builder annotation so that it can create objects with all the fields
//when you call the .build() method 


@NoArgsConstructor //Required when a JSON request is made at your api endpoint which is needed to be converted into a 
// java object. Spring handles this task to its internal library Jackson which does this task in two step process 
// 1. create a completely blank object ---> requires No-args constructor
// 2. fill the fields of the object with the data from the JSON request

/*
    Consider a scenario where you are creating a POST request to your endpoing /login
    and you are sending a JSON request with the following field

    {
    "email": "test@123",
    "password": "123"
    }

    Spring handles the task of converting this JSON data into AuthDTO(present as a parameter to the login method)
    to Jackson an internal Java library. Jackson does the task in two steps 
    1. AuthDTO authDTO = new AuthDTO(); ------> requires No-args constructor
    2. authDTO.setEmail("test@123");
       authDTO.setPassword("123");

    
 */
@Builder 
public class AuthDTO {
    private String email;
    private String password;
    private String token;
}
