package com.sk8.swashbucklers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * Data transfer object for login information
 * @author Daniel Bernier
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @Email
    private String email;
    @Size(min = 1)
    private String password;
}
