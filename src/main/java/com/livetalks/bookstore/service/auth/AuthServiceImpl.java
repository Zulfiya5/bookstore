package com.livetalks.bookstore.service.auth;

import com.livetalks.bookstore.entity.user.User;
import com.livetalks.bookstore.exception.RestException;
import com.livetalks.bookstore.model.receive.user.UserSignInModel;
import com.livetalks.bookstore.model.receive.user.UserSignUpModel;
import com.livetalks.bookstore.model.response.user.TokenModel;
import com.livetalks.bookstore.model.response.user.UserDisplayModel;
import com.livetalks.bookstore.repository.UserRepository;
import com.livetalks.bookstore.security.JWTProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            @Lazy AuthenticationManager authenticationManager,
            JWTProvider jwtProvider,
            @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDisplayModel signUpUser(UserSignUpModel userSignUpModel) {
        if (existsByUsername(userSignUpModel.getUsername()))
            throw new RestException(HttpStatus.CONFLICT,
                    "Username is already taken, try another one");
        User user = userSignUpModelToUser(userSignUpModel);
        User savedUser = saveUser(user);
        return userToUserDisplayModel(savedUser);
    }

    @Override
    public TokenModel signInUser(UserSignInModel userSignInModel) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userSignInModel.getUsername(),
                                    userSignInModel.getPassword()
                            )
                    );
            User user = (User) authentication.getPrincipal();
            String refreshToken = jwtProvider.generateTokenFromId(user.getId(), false);
            String accessToken = jwtProvider.generateTokenFromId(user.getId(), true);
            return new TokenModel(accessToken, refreshToken);
        } catch (Exception e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Password or username incorrect!");
        }
    }

    @Override
    public TokenModel refreshToken(TokenModel tokenDto) {
        try {
            jwtProvider.validateToken(tokenDto.getAccessToken());
            return tokenDto;
        } catch (ExpiredJwtException e) {
            try {
                jwtProvider.validateToken(tokenDto.getRefreshToken());
                UUID userId = UUID.fromString(jwtProvider.getIdFromToken(tokenDto.getRefreshToken()));
                return new TokenModel(
                        jwtProvider.generateTokenFromId(userId, true),
                        jwtProvider.generateTokenFromId(userId, false)
                );
            } catch (Exception ex) {
                throw new RestException(HttpStatus.UNAUTHORIZED, "Refresh token is broken");
            }
        } catch (Exception e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Access token is broken");
        }
    }

    @Override
    public UserDetails loadById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private User userSignUpModelToUser(UserSignUpModel userSignUpModel) {
        return new User(
                userSignUpModel.getFirstName(),
                userSignUpModel.getLastName(),
                userSignUpModel.getUsername(),
                passwordEncoder.encode(userSignUpModel.getPassword())
        );
    }

    private UserDisplayModel userToUserDisplayModel(User user) {
        return new UserDisplayModel(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername()
        );
    }

}
