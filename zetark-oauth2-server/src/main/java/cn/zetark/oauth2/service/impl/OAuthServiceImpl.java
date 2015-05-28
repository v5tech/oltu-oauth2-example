package cn.zetark.oauth2.service.impl;

import cn.zetark.oauth2.service.ClientService;
import cn.zetark.oauth2.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service("OAuthServiceImpl")
public class OAuthServiceImpl implements OAuthService {

    private Cache cache;

    @Autowired
    private ClientService clientService;

    @Autowired
    public OAuthServiceImpl(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("code-cache");
    }

    @Override
    public void addAuthCode(String authCode, String username) {
        cache.put(authCode, username);
    }

    @Override
    public void addAccessToken(String accessToken, String username) {
        cache.put(accessToken, username);
    }

    @Override
    public String getUsernameByAuthCode(String authCode) {
        return (String)cache.get(authCode).get();
    }

    @Override
    public String getUsernameByAccessToken(String accessToken) {
        return (String)cache.get(accessToken).get();
    }

    @Override
    public boolean checkAuthCode(String authCode) {
        return cache.get(authCode) != null;
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        return cache.get(accessToken) != null;
    }

    @Override
    public boolean checkClientId(String clientId) {
        return clientService.findByClientId(clientId) != null;
    }

    @Override
    public boolean checkClientSecret(String clientSecret) {
        return clientService.findByClientSecret(clientSecret) != null;
    }

    @Override
    public long getExpireIn() {
        return 3600L;
    }
}
