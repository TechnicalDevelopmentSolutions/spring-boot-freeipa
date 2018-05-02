package com.techdevsolutions.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdevsolutions.beans.Response;
import com.techdevsolutions.beans.auditable.User;
import com.techdevsolutions.service.InstallerService;
import com.techdevsolutions.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/v1/app")
public class AppController extends BaseController {
    private Environment environment;
    private InstallerService installerService;
    private UserService userService;

    @Autowired
    public AppController(Environment environment, InstallerService installerService, UserService userService) {
        this.environment = environment;
        this.installerService = installerService;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object search(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("name", environment.getProperty("application.name"));
            map.put("createdBy", environment.getProperty("application.createdBy"));
            map.put("title", environment.getProperty("application.title"));
            map.put("version", environment.getProperty("application.version"));
            map.put("buildNumber", environment.getProperty("application.buildNumber"));
            map.put("buildDateTime", environment.getProperty("application.buildDateTime"));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
//            Map<String, Object> userObject = new HashMap<>();
//            userObject.put("username", principal.get("value"));
//            userObject.put("principal", principal);
//            userObject.put("authorities", authentication.getAuthorities());

            Integer userId = User.GetUIdFromPrincipal(principal);
            User user = this.userService.get(userId);

            // If this is kept in, more roles will be added to the demo user object
//            for (GrantedAuthority ga : authorities) {
//                Role role = new Role();
//                role.setName(ga.getAuthority());
//                user.getRoles().add(role);
//            }

            Map<String, Object> userAsMap = new ObjectMapper().convertValue(user, Map.class);
            userAsMap.put("principal", principal);
            userAsMap.put("authorities", authentication.getAuthorities());
            map.put("user", userAsMap);
            return new Response(map, this.getTimeTook(request));
        } catch (Exception e) {
            e.printStackTrace();
            return this.generateErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value = "install", method = RequestMethod.GET)
    public Object install(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.installerService.install();
            return new Response(null, this.getTimeTook(request));
        } catch (Exception e) {
            e.printStackTrace();
            return this.generateErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

}