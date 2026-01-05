package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.mapper;

import com.winnersystems.smartparking.auth.application.dto.command.ChangePasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ChangePasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ForgotPasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.LoginRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ResetPasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre REST DTOs y Application DTOs.
 *
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>REST Request → Application Command</li>
 *   <li>Application Response → REST Response</li>
 *   <li>Extraer metadata del HttpServletRequest (IP, User-Agent)</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class AuthRestMapper {

   // ========== REQUEST → COMMAND ==========

   /**
    * LoginRequest → LoginCommand.
    */
   public LoginCommand toCommand(LoginRequest request, HttpServletRequest httpRequest) {
      return new LoginCommand(
            request.email(),                // 1. email
            request.password(),             // 2. password
            request.captchaToken(),         // 3. captchaToken
            request.rememberMe(),           // 4. rememberMe
            extractDeviceInfo(httpRequest), // 5. deviceInfo
            extractIpAddress(httpRequest)   // 6. ipAddress
      );
   }

   /**
    * ForgotPasswordRequest → ForgotPasswordCommand.
    */
   public ForgotPasswordCommand toCommand(ForgotPasswordRequest request, HttpServletRequest httpRequest) {
      return new ForgotPasswordCommand(
            request.email(),
            request.captchaToken(),
            extractIpAddress(httpRequest),
            extractUserAgent(httpRequest)
      );
   }

   /**
    * ResetPasswordRequest → ResetPasswordCommand.
    */
   public ResetPasswordCommand toCommand(ResetPasswordRequest request, HttpServletRequest httpRequest) {
      return new ResetPasswordCommand(
            request.token(),                // 1. token
            request.newPassword(),          // 2. newPassword
            request.confirmPassword(),      // 3. confirmPassword
            extractIpAddress(httpRequest),  // 4. ipAddress
            extractUserAgent(httpRequest)   // 5. userAgent
      );
   }

   // ========== RESPONSE DTO → REST RESPONSE ==========

   /**
    * AuthResponseDto → LoginResponse.
    */
   public LoginResponse toLoginResponse(AuthResponseDto dto) {
      LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
            dto.user().id(),
            dto.user().firstName(),
            dto.user().lastName(),
            dto.user().firstName() + " " + dto.user().lastName(),
            dto.user().email(),
            dto.user().profilePicture(),
            dto.user().status() ? "ACTIVE" : "INACTIVE"
      );

      return new LoginResponse(
            dto.accessToken(),
            dto.refreshToken(),
            dto.tokenType(),
            dto.expiresIn(),
            userInfo
      );
   }

   // ========== HELPERS: EXTRAER INFO DEL REQUEST ==========

   /**
    * Extrae la dirección IP real del cliente.
    */
   private String extractIpAddress(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");

      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
         return ip.split(",")[0].trim();
      }

      ip = request.getHeader("X-Real-IP");
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
         return ip;
      }

      ip = request.getRemoteAddr();

      if ("0:0:0:0:0:0:0:1".equals(ip)) {
         ip = "127.0.0.1";
      }

      return ip;
   }

   /**
    * Extrae el User-Agent del cliente.
    */
   private String extractUserAgent(HttpServletRequest request) {
      String userAgent = request.getHeader("User-Agent");
      return (userAgent != null && !userAgent.isEmpty()) ? userAgent : "Unknown";
   }

   /**
    * Extrae información del dispositivo (User-Agent).
    */
   private String extractDeviceInfo(HttpServletRequest request) {
      return extractUserAgent(request);
   }

   /**
    * Convierte ChangePasswordRequest → ChangePasswordCommand
    */
   public ChangePasswordCommand toCommand(
         ChangePasswordRequest request,
         HttpServletRequest httpRequest,
         Long userId) {

      return new ChangePasswordCommand(
            userId,
            request.currentPassword(),
            request.newPassword(),
            request.confirmPassword(),
            extractIpAddress(httpRequest),
            extractUserAgent(httpRequest)
      );
   }
}