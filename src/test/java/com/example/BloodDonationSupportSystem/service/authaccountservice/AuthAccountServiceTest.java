package com.example.BloodDonationSupportSystem.service.authaccountservice;

import com.example.BloodDonationSupportSystem.dto.authenaccountDTO.request.LoginRequest;
import com.example.BloodDonationSupportSystem.dto.authenaccountDTO.request.RegisterRequest;
import com.example.BloodDonationSupportSystem.dto.authenaccountDTO.response.LoginAccountResponse;
import com.example.BloodDonationSupportSystem.dto.authenaccountDTO.response.RegisterAccountReponse;
import com.example.BloodDonationSupportSystem.entity.RoleEntity;
import com.example.BloodDonationSupportSystem.entity.UserEntity;
import com.example.BloodDonationSupportSystem.exception.BadRequestException;
import com.example.BloodDonationSupportSystem.exception.ResourceNotFoundException;
import com.example.BloodDonationSupportSystem.repository.RoleRepository;
import com.example.BloodDonationSupportSystem.repository.UserRepository;
import com.example.BloodDonationSupportSystem.service.jwtservice.JwtService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;



import static org.testng.Assert.*;

public class AuthAccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthAccountService authAccountService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test register thành công
    @Test
    public void testRegister_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPhoneNumber("123456789");
        registerRequest.setFullName("John Doe");
        registerRequest.setAddress("123 ABC Street");
        registerRequest.setDateOfBirth(null);
        registerRequest.setGender("Male");
        registerRequest.setStatus("Active");
        registerRequest.setConfirmPassword("password123");

        when(userRepository.existsByPhoneNumber("123456789")).thenReturn(false);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("ROLE_MEMBER");

        when(roleRepository.findByRoleName("ROLE_MEMBER")).thenReturn(Optional.of(roleEntity));
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        RegisterAccountReponse response = authAccountService.register(registerRequest);
        Assert.assertEquals(response.getMessage(), "Registration successful");

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    // Test register khi số điện thoại đã tồn tại
    @Test(expectedExceptions = BadRequestException.class)
    public void testRegister_PhoneNumberExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPhoneNumber("123456789");

        when(userRepository.existsByPhoneNumber("123456789")).thenReturn(true);

        authAccountService.register(registerRequest);
    }

    // Test register khi role không tìm thấy
    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testRegister_RoleNotFound() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPhoneNumber("123456789");
        registerRequest.setConfirmPassword("password123");

        when(userRepository.existsByPhoneNumber("123456789")).thenReturn(false);
        when(roleRepository.findByRoleName("ROLE_MEMBER")).thenReturn(Optional.empty());

        authAccountService.register(registerRequest);
    }

    // Data Provider cho login
    @DataProvider(name = "loginDataProvider")
    public Object[][] loginDataProvider() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setPhoneNumber("123456789");
        userEntity.setPasswordHash("encoded_password");
        RoleEntity role = new RoleEntity();
        role.setRoleName("ROLE_MEMBER");
        userEntity.setRole(role);

        return new Object[][]{
                // phoneNumber, password, userEntity, userExists, shouldAuthenticateSuccess, expectException

                // Case 1: Login thành công
                {"123456789", "password123", userEntity, true, true, false},

                // Case 2: User không tồn tại
                {"000000000", "password123", null, false, false, true},

                // Case 3: User tồn tại nhưng authentication fail
                {"123456789", "wrongPassword", userEntity, true, false, true}
        };
    }

    // Test login có DataProvider
    @Test(dataProvider = "loginDataProvider")
    public void testAuthAccount(String phoneNumber, String password, UserEntity userEntity, boolean userExists, boolean shouldAuthenticateSuccess, boolean expectException) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhoneNumber(phoneNumber);
        loginRequest.setPassword(password);

        if (userExists) {
            when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(userEntity));
        } else {
            when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        }

        if (shouldAuthenticateSuccess && userExists) {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mock(Authentication.class));
            when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");
        } else if (userExists) {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadRequestException("Invalid credentials"));
        }

        try {
            LoginAccountResponse response = authAccountService.authAccount(loginRequest);
            if (expectException) {
                Assert.fail("Expected exception but none thrown");
            } else {
                Assert.assertEquals(response.getToken(), "fake-jwt-token");
            }
        } catch (Exception ex) {
            if (!expectException) {
                Assert.fail("Unexpected exception thrown: " + ex.getMessage());
            }
        }
    }
}