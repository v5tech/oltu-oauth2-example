package cn.zetark.oauth2.client;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class OauthClient {

    /**
     * 获取授权码
     * @return
     * @throws OAuthSystemException
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
	private static Response makeAuthCodeRequest() throws OAuthSystemException,
			MalformedURLException, URISyntaxException {

		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(ClientParams.OAUTH_SERVER_URL) // oauth2 认证授权地址
				.setClientId(ClientParams.CLIENT_ID) // CLIENT_ID
				.setRedirectURI(ClientParams.OAUTH_SERVER_REDIRECT_URI) // 回调地址
				.setResponseType(ResponseType.CODE.toString()) // 返回类型值
				.buildQueryMessage();

        // 创建表单，模拟填充表单并提交表单
        Form form = new Form();
        form.param("username",ClientParams.USERNAME);
        form.param("password",ClientParams.PASSWORD);
        form.param("client_id",ClientParams.CLIENT_ID);
        form.param("code",ResponseType.CODE.toString());
        form.param("redirect_uri",ClientParams.OAUTH_SERVER_REDIRECT_URI);

        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target(request.getLocationUri())
                .request()
                .post(Entity.form(form));
		System.out.println(response.getStatus());

        String location = response.getLocation().toURL().toString();

        System.out.println(response.getLocation());

        String authCode = location.substring(location.lastIndexOf("=")+1);

        try {
            System.out.println(authCode);
            makeTokenRequestWithAuthCode(authCode);
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } finally {

        }

        return response;
	}

    /**
     * 根据授权码获取accessToken
     * @param authCode
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
	private static OAuthAccessTokenResponse makeTokenRequestWithAuthCode(String authCode) throws OAuthProblemException, OAuthSystemException {

        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(ClientParams.OAUTH_SERVER_TOKEN_URL)
                .setClientId(ClientParams.CLIENT_ID)
                .setClientSecret(ClientParams.CLIENT_SECRET)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setCode(authCode)
                .setRedirectURI(ClientParams.OAUTH_SERVER_REDIRECT_URI)
                .buildBodyMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(request);

        System.out.println("Access Token: " + oauthResponse.getAccessToken());
        System.out.println("Expires In: " + oauthResponse.getExpiresIn());

        getAuthedService(oauthResponse.getAccessToken());

        return oauthResponse;
    }

    /**
     * 测试开放接口服务
     */
    private static void getAuthedService(String accessToken){
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target(ClientParams.OAUTH_SERVICE_API)
                .queryParam("access_token",accessToken)
                .request()
                .get();
        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }

    public static void main(String[] args) throws Exception {

        makeAuthCodeRequest();

    }


}
