package com.livetalks.bookstore.controller.auth;

import com.livetalks.bookstore.model.response.user.TokenModel;
import com.livetalks.bookstore.model.receive.user.UserSignInModel;
import com.livetalks.bookstore.model.receive.user.UserSignUpModel;
import com.livetalks.bookstore.model.response.ApiResult;
import com.livetalks.bookstore.model.response.user.UserDisplayModel;
import com.livetalks.bookstore.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ApiResult<TokenModel> signIn(UserSignInModel userSignInModel) {

        return ApiResult.successResponse(authService.signInUser(userSignInModel));
    }

    @Override
    public ApiResult<UserDisplayModel> signUp(UserSignUpModel userSignUpModel) {
        return ApiResult.successResponse(authService.signUpUser(userSignUpModel));
    }

    @Override
    public ApiResult<TokenModel> refreshToken(TokenModel tokenModel) {
        return ApiResult.successResponse(authService.refreshToken(tokenModel));
    }
}
