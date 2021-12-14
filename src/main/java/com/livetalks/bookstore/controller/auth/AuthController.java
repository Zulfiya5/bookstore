package com.livetalks.bookstore.controller.auth;

import com.livetalks.bookstore.constants.RestConstant;
import com.livetalks.bookstore.model.response.user.TokenModel;
import com.livetalks.bookstore.model.receive.user.UserSignInModel;
import com.livetalks.bookstore.model.receive.user.UserSignUpModel;
import com.livetalks.bookstore.model.response.ApiResult;
import com.livetalks.bookstore.model.response.user.UserDisplayModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(RestConstant.AUTH_CONTROLLER)
public interface AuthController {

    String SIGN_IN = "/sign-in";
    String SIGN_UP = "/sign-up";
    String REFRESH_TOKEN = "/refresh-token";


    /**
     * Registering the new user (username must be unique)
     *
     * @param userSignUpModel (firstName, lastName, username, password)
     * @return TokenModel(accessToken, refreshToken)
     */
    @PostMapping(SIGN_UP)
    ApiResult<UserDisplayModel> signUp(@RequestBody @Valid UserSignUpModel userSignUpModel);

    /**
     *Signing in to the Service
     *
     * @param userSignInModel (username, password)
     * @return TokenModel (accessToken , refreshToken)
     */
    @PostMapping(SIGN_IN)
    ApiResult<TokenModel> signIn(@RequestBody @Valid UserSignInModel userSignInModel);

    /**
     * in case of accessToken expiration, renewing token using refreshToken
     *
     * @param tokenModel (accessToken,refreshToken)
     * @return TokenModel(accessToken, refreshToken)
     */
    @PostMapping(REFRESH_TOKEN)
    ApiResult<TokenModel> refreshToken(@RequestBody TokenModel tokenModel);



}
