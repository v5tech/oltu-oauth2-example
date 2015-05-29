package cn.zetark.oauth2.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.ws.rs.HttpMethod;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/29.
 */
public class UrlClient {

    /**
     * 获取授权码
     *
     * @return
     */
    private static String getAuthCode() throws Exception {

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("username", ClientParams.USERNAME);
        params.put("password", ClientParams.PASSWORD);
        params.put("client_id", ClientParams.CLIENT_ID);
        params.put("response_type", ResponseType.CODE.toString());
        params.put("redirect_uri", ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postStr.length() != 0) {
                postStr.append('&');
            }
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        connection.setInstanceFollowRedirects(false);// 必须设置该属性
        String location = connection.getHeaderField("Location");
        System.out.println(location);
        return location.substring(location.indexOf("=") + 1);
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    private static String getAccessToken(String authCode) throws Exception {

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("client_id", ClientParams.CLIENT_ID);
        params.put("client_secret", ClientParams.CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);
        params.put("redirect_uri", ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postStr.length() != 0) {
                postStr.append('&');
            }
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = gson.fromJson(response.toString(), Map.class);
        String accessToken = map.get("access_token");
        System.out.println(accessToken);
        return accessToken;
    }


    /**
     * 获取accessToken
     *
     * @return
     */
    private static void getService(String accessToken) throws Exception {

        URL url = new URL(ClientParams.OAUTH_SERVICE_API + "?access_token=" + accessToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpMethod.GET);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
    }


    public static void main(String[] args) throws Exception {
        String authCode = getAuthCode();
        String accessToken = getAccessToken(authCode);
        getService(accessToken);
    }
}
