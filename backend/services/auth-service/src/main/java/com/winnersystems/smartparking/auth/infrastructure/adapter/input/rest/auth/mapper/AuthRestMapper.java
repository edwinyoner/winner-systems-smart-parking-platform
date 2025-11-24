package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.mapper;

import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ForgotPasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.LoginRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ResetPasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.LoginResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre REST DTOs y Application DTOs.
 *
 * REST Request → Application Command
 * Application Response → REST Response
 */
@Component
public class AuthRestMapper {

   /**
    * LoginRequest → LoginCommand
    */
   public LoginCommand toCommand(LoginRequest request) {
      return new LoginCommand(
            request.email(),
            request.password(),
            request.captchaToken(),
            request.rememberMe()
      );
   }

   /**
    * ForgotPasswordRequest → ForgotPasswordCommand
    */
   public ForgotPasswordCommand toCommand(ForgotPasswordRequest request) {
      return new ForgotPasswordCommand(
            request.email(),
            request.captchaToken()
      );
   }

   /**
    * ResetPasswordRequest → ResetPasswordCommand
    */
   public ResetPasswordCommand toCommand(ResetPasswordRequest request) {
      return new ResetPasswordCommand(
            request.token(),
            request.newPassword(),
            request.confirmPassword()
      );
   }

   /**
    * AuthResponseDto → LoginResponse
    */
   public LoginResponse toLoginResponse(AuthResponseDto dto) {
      LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
            dto.user().id(),
            dto.user().firstName(),
            dto.user().lastName(),
            dto.user().fullName(),
            dto.user().email(),
            dto.user().profilePicture(),
            dto.user().status().name()
      );

      return new LoginResponse(
            dto.accessToken(),
            dto.refreshToken(),
            dto.tokenType(),
            dto.expiresIn(),
            userInfo
      );
   }
}