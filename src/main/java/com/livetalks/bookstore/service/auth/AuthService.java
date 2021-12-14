package com.livetalks.bookstore.service.auth;

import com.livetalks.bookstore.model.response.user.TokenModel;
import com.livetalks.bookstore.model.receive.user.UserSignInModel;
import com.livetalks.bookstore.model.receive.user.UserSignUpModel;
import com.livetalks.bookstore.model.response.ApiResult;
import com.livetalks.bookstore.model.response.user.UserDisplayModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AuthService extends UserDetailsService {

    UserDisplayModel signUpUser(UserSignUpModel userSignUpModel);

    TokenModel signInUser(UserSignInModel userSignInModel);

    TokenModel refreshToken(TokenModel tokenModel);

    UserDetails loadById(UUID id);

}
