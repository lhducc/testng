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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthAccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterAccountReponse register(RegisterRequest registerRequest) {
        RegisterAccountReponse reponse;
        if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new BadRequestException("Phone number already in use");
        }
            UserEntity user = new UserEntity();
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            Optional<RoleEntity> roleMember = roleRepository.findByRoleName("ROLE_MEMBER");
            user.setRole(roleMember.orElseThrow(() -> new ResourceNotFoundException("Cannot find role")));
            user.setFullName(registerRequest.getFullName());
            user.setAddress(registerRequest.getAddress());
            user.setDateOfBirth(registerRequest.getDateOfBirth());
            user.setGender(registerRequest.getGender());


            user.setStatus(registerRequest.getStatus());
            user.setPasswordHash(passwordEncoder.encode(registerRequest.getConfirmPassword()));
        System.out.println(user.getFullName() + " " +user.getGender() + " " + user.getStatus() + " " + user.getAddress());
            userRepository.save(user);
            reponse = new RegisterAccountReponse();
            reponse.setMessage("Registration successful");





        return reponse;
    }

    public LoginAccountResponse authAccount(LoginRequest loginRequest) {
        LoginAccountResponse loginAccountResponse;
        UserEntity user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber()).orElseThrow(() -> new BadRequestException("PhoneNumber doesn't exist"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));
        String token = jwtService.generateToken(new User(user.getUserId().toString(), user.getPasswordHash(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleName()))));
        loginAccountResponse = new LoginAccountResponse(token);
        return loginAccountResponse;


    }






}
