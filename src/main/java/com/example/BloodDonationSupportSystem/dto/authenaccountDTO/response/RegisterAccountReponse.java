package com.example.BloodDonationSupportSystem.dto.authenaccountDTO.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RegisterAccountReponse {
    @NotNull
    private String message;
}
