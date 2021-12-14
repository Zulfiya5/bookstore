package com.livetalks.bookstore;


import com.livetalks.bookstore.entity.user.User;
import com.livetalks.bookstore.exception.RestException;
import com.livetalks.bookstore.model.receive.user.UserSignInModel;
import com.livetalks.bookstore.model.receive.user.UserSignUpModel;
import com.livetalks.bookstore.model.response.user.TokenModel;
import com.livetalks.bookstore.model.response.user.UserDisplayModel;
import com.livetalks.bookstore.repository.UserRepository;
import com.livetalks.bookstore.security.JWTProvider;
import com.livetalks.bookstore.service.auth.AuthServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JWTProvider jwtProvider;

    @InjectMocks
    AuthServiceImpl authService;


    @Test
    public void shouldReturnUserDisplayModelWhenSavedUserSuccessfully() {
        UserSignUpModel userSignUpModel = new UserSignUpModel();
        userSignUpModel.setFirstName("John");
        userSignUpModel.setLastName("Smith");
        userSignUpModel.setUsername("01737186095");
        userSignUpModel.setPassword("123");
        User user = new User(
                userSignUpModel.getFirstName(),
                userSignUpModel.getLastName(),
                userSignUpModel.getUsername(),
                passwordEncoder.encode(userSignUpModel.getPassword())
        );
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        UserDisplayModel userDisplayModel = new UserDisplayModel(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername()
        );
        UserDisplayModel userDisplayModel1 = authService.signUpUser(userSignUpModel);
        assertThat(userDisplayModel1.getUsername()).isNotNull();
        assertEquals(userDisplayModel, userDisplayModel1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldThrowErrorWhenSavingUserWithExistingUsername() {
        UserSignUpModel userSignUpModel = new UserSignUpModel();
        userSignUpModel.setFirstName("John");
        userSignUpModel.setLastName("Smith");
        userSignUpModel.setUsername("01737186095");
        userSignUpModel.setPassword("123");
        User user = new User(
                userSignUpModel.getFirstName(),
                userSignUpModel.getLastName(),
                userSignUpModel.getUsername(),
                passwordEncoder.encode(userSignUpModel.getPassword())
        );
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(RestException.class, () ->
            authService.signUpUser(userSignUpModel)
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldReturnTokenWhenSignedInUserSuccessfully() {
        UserSignInModel userSignInModel = new UserSignInModel();
        userSignInModel.setUsername("01737186095");
        userSignInModel.setPassword("123");
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userSignInModel.getUsername(),
                        userSignInModel.getPassword()
                );
        lenient().when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        UUID userId = UUID.fromString("94ad45b7-4c46-43cc-9636-654615802bbc");
        String refreshToken = "";
        String accessToken = "";
        lenient().when(jwtProvider.generateTokenFromId(userId, true)).thenReturn(accessToken);
        lenient().when(jwtProvider.generateTokenFromId(userId, false)).thenReturn(refreshToken);
        TokenModel tokenModel = new TokenModel(accessToken, refreshToken);
        assertThat(tokenModel).isNotNull();
    }
}
