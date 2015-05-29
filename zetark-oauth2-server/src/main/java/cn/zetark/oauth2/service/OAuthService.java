package cn.zetark.oauth2.service;

public interface OAuthService {

    //添加 auth code
    void addAuthCode(String authCode, String username);

    //添加 access token
    void addAccessToken(String accessToken, String username);

    //验证auth code是否有效
    boolean checkAuthCode(String authCode);

    //验证access token是否有效
    boolean checkAccessToken(String accessToken);

    String getUsernameByAuthCode(String authCode);

    String getUsernameByAccessToken(String accessToken);


    //auth code / access token 过期时间
    long getExpireIn();


    boolean checkClientId(String clientId);

    boolean checkClientSecret(String clientSecret);


}
