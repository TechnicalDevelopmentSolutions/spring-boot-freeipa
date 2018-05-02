package com.techdevsolutions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class FreeIpaAuthenticationManager implements AuthenticationProvider {
    private Logger logger = Logger.getLogger(FreeIpaAuthenticationManager.class.getName());
    private final String LOGIN_SERVICE = "/ipa/session/login_password";
    private final String LOOKUP_SERVICE = "/ipa/session/json";
    private final String LOOKUP_REFERER = "/ipa/ui/";

    private Environment environment;
    private String baseUrl = "";

    @Autowired
    public FreeIpaAuthenticationManager(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        this.baseUrl = this.environment.getProperty("freeipa.base.url");

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

//        this.logger.info("FreeIpaAuthenticationManager - authenticating: " + username);
//        this.logger.info("password: " + password);

        Map<String, Object> lookup = this.auth(username, password);
        Boolean authenticated = (Boolean) lookup.get("authenticated");
        Map<String, Object> principal = (Map<String, Object>) lookup.get("principal");
        List<GrantedAuthority> roles = (List<GrantedAuthority>) lookup.get("authorities");

        if (authenticated) {
            return new UsernamePasswordAuthenticationToken(principal, password, roles);
        } else {
            throw new BadCredentialsException("FreeIpaAuthenticationManager - Authentication failed for: " + username);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Map<String, Object> auth(String username, String password) {
        Map<String, Object> lookup = new HashMap<>();
        lookup.put("authenticated", false);
        lookup.put("principal", new HashMap<String, Object>());
        lookup.put("authorities", new ArrayList<GrantedAuthority>());

        try {
            CloseableHttpClient client = HttpClients.custom().
                    setHostnameVerifier(new AllowAllHostnameVerifier()).
                    setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
                    {
                        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                        {
                            return true;
                        }
                    }).build()).build();

//            CloseableHttpClient client = HttpClients.createDefault();
            this.logger.info("FreeIpaAuthenticationManager - auth - URL: " + this.baseUrl + LOGIN_SERVICE);
            HttpPost httpPost = new HttpPost(this.baseUrl + LOGIN_SERVICE);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user", username));
            params.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse response = client.execute(httpPost);
            Integer statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String cookie = "";

                for (Header header : response.getAllHeaders()) {
                    if (header.getName().equals("Set-Cookie")) {
                        cookie = header.getValue();
                    }
                }

                if (StringUtils.isNotEmpty(cookie)) {
                    lookup = this.lookup(client, cookie, username, lookup);
                } else {
                    this.logger.info("FreeIpaAuthenticationManager - auth - error - cookie is empty");
                }
            } else if(statusCode == 401) {
//                this.logger.info("MyUserDetailsService - auth - Unauthorized - response: " + response.toString());
                this.logger.info("FreeIpaAuthenticationManager - auth - Unauthorized");
            } else {
//                this.logger.info("MyUserDetailsService - auth - error - response: " + response.toString());
                this.logger.info("FreeIpaAuthenticationManager - auth - error - statusCode: " + statusCode);
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lookup;
    }

    private Map<String, Object> lookup(CloseableHttpClient client, String cookie, String username, Map<String, Object> lookup) throws IOException {
        HttpPost httpPost = new HttpPost(this.baseUrl + LOOKUP_SERVICE);
        httpPost.setHeader("Cookie", cookie);
        httpPost.setHeader("Referer",this.baseUrl +  LOOKUP_REFERER);
        httpPost.setHeader("Content-Type", "application/json");
        HttpEntity entity = new NStringEntity("{\"method\":\"user_show/1\",\"params\":[[\"" + username + "\"],{\"all\":true,\"version\":\"2.229\"}]}", ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = client.execute(httpPost);
        Integer statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            String responseBody = EntityUtils.toString(response.getEntity());

//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            JsonParser jp = new JsonParser();
//            JsonElement je = jp.parse(responseBody);
//            String prettyJsonString = gson.toJson(je);
//            this.logger.info("MyUserDetailsService - lookup - responseBody: " + prettyJsonString);

            Map<String, Object> map = new ObjectMapper().readValue(responseBody, Map.class);

            if (map != null) {
                Map<String, Object> principal = (Map<String, Object>) map.get("result");

                if (principal != null) {
                    Map<String, Object> principalDetails = (Map<String, Object>) principal.get("result");

                    if (principalDetails != null) {
                        List<String> groups = (List<String>) principalDetails.get("memberof_group");

                        if (groups != null) {
                            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

                            for (String group : groups) {
                                GrantedAuthority authority = new SimpleGrantedAuthority(group);
                                authorities.add(authority);
                            }

                            lookup.put("authenticated", true);
                            lookup.put("principal", principal);
                            lookup.put("authorities", authorities);
                            this.logger.info("FreeIpaAuthenticationManager - authenticated: " + username + ", authorities: " + authorities);
                        } else {
                            this.logger.info("FreeIpaAuthenticationManager - lookup - error - Unexpected response (groups)");
                        }
                    } else {
                        this.logger.info("FreeIpaAuthenticationManager - lookup - error - Unexpected response (principalDetails)");
                    }
                } else {
                    this.logger.info("FreeIpaAuthenticationManager - lookup - error - Unexpected response (principal)");
                }
            } else {
                this.logger.info("FreeIpaAuthenticationManager - lookup - error - Unexpected response (map)");
            }
        } else if (statusCode == 401) {
//            this.logger.info("MyUserDetailsService - lookup - Unauthorized - response: " + response.toString());
            this.logger.info("FreeIpaAuthenticationManager - lookup - Unauthorized");
        } else {
//            this.logger.info("MyUserDetailsService - lookup - error - response: " + response.toString());
            this.logger.info("FreeIpaAuthenticationManager - lookup - error - statusCode: " + statusCode);
        }

        return lookup;
    }
}
