package com.example.bookstore.service;

import com.example.bookstore.entity.TokenConfirm;
import com.example.bookstore.entity.Users;
import com.example.bookstore.exception.BadRequestException;
import com.example.bookstore.model.enums.TokenType;
import com.example.bookstore.model.enums.UserRole;
import com.example.bookstore.model.request.LoginRequest;
import com.example.bookstore.model.request.RegisterRequest;
import com.example.bookstore.repository.TokenConfirmRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.response.VerifyResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final HttpSession session;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfirmRepository tokenConfirmRepository;
    private final MailService mailService;

    public void login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get the user details from the repository
            Users authenticatedUser = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Store the authenticated user in the session
            session.setAttribute("currentUser", authenticatedUser);

            // Optionally store just the username or any other session attributes
            session.setAttribute("MY_SESSION", authentication.getName());

        } catch (DisabledException e) {
            throw new BadRequestException("Tài khoản chưa được kích hoạt");
        } catch (AuthenticationException e) {
            throw new BadRequestException("Email hoặc mật khẩu không đúng");
        }
    }

    public void register(RegisterRequest request) {
        Optional<Users> usersOptional = userRepository.findByEmail(request.getEmail());
        if (usersOptional.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        // Encode password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Convert the user's name to uppercase for the profile picture text
        String upperCase = request.getName().toUpperCase();

        // Create new user
        Users user = Users.builder()
                .userName(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)  // Save encoded password
                .enabled(false)
                .role(UserRole.USER)
                .enabled(true)
                .profilePicture("https://placehold.co/50x50?text=" + upperCase)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

//        // Create a token for verification
//        TokenConfirm token = TokenConfirm.builder()
//                .token(UUID.randomUUID().toString())
//                .type(TokenType.REGISTRATION)
//                .createdAt(LocalDateTime.now())
//                .expiresAt(LocalDateTime.now().plusMinutes(30))
//                .user(user)
//                .build();
//        tokenConfirmRepository.save(token);
//
//        // Create the verification link
//        String link = "http://localhost:8080/confirmation?token=" + token.getToken();
//
//        // Send verification email
//        mailService.sendMail2(user, "Xác thực tài khoản", link);
    }

//    public VerifyResponse confirmRegistration(String token) {
//        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
//                .findByTokenAndType(token, TokenType.REGISTRATION);
//
//        // Check if the token exists
//        if (tokenConfirmOptional.isEmpty()) {
//            return VerifyResponse.builder()
//                    .message("Token không hợp lệ")
//                    .success(false)
//                    .build();
//        }
//
//        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
//
//        // Check if the token is already confirmed
//        if (tokenConfirm.getConfirmedAt() != null) {
//            return VerifyResponse.builder()
//                    .message("Token đã được xác thực")
//                    .success(false)
//                    .build();
//        }
//
//        // Check if the token has expired
//        if (tokenConfirm.getExpiresAt().isBefore(LocalDateTime.now())) {
//            return VerifyResponse.builder()
//                    .message("Token đã hết hạn")
//                    .success(false)
//                    .build();
//        }
//
//        // Activate the user account
//        Users user = tokenConfirm.getUser();
//        user.setEnabled(true);
//        userRepository.save(user);
//
//        // Confirm the token
//        tokenConfirm.setConfirmedAt(LocalDateTime.now());
//        tokenConfirmRepository.save(tokenConfirm);
//
//        return VerifyResponse.builder()
//                .message("Xác thực tài khoản thành công")
//                .success(true)
//                .build();
//    }
}