package com.example.BloodDonationSupportSystem.dto.authenaccountDTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @NotBlank(message = "Phonenumber can't blank")
    @Length(min = 10, max = 10, message = "PhoneNumber must be 10 numbers")
    private String phoneNumber;

    @NotBlank(message = "Password can't blank")
    @Length(min = 6, message = "Password less than 6 characters")
    private String password;
}
