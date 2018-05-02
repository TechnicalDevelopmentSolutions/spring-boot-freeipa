package com.techdevsolutions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringWebSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = Logger.getLogger(SpringWebSecurityConfig.class.getName());

    Environment environment;
    FreeIpaAuthenticationManager freeIpaAuthenticationManager;

    @Autowired
    public SpringWebSecurityConfig(Environment environment, FreeIpaAuthenticationManager freeIpaAuthenticationManager) {
        this.environment = environment;
        this.freeIpaAuthenticationManager = freeIpaAuthenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
            .authorizeRequests()
                .antMatchers(
//                        "/*",
//                        "/assets/**"
                )
                    .permitAll()
//                .antMatchers("/api/v1/app/**")
//                    .hasRole("USER")
//                .antMatchers("/api/v1/**")
//                    .hasRole("API")
                .anyRequest()
                    .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(freeIpaAuthenticationManager);
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        String url = this.environment.getProperty("ldap.urls");
//        String baseBn = this.environment.getProperty("ldap.base.dn");
//        String ldapSecurityPrincipal = this.environment.getProperty("ldap.username");
//        String ldapPrincipalPassword = this.environment.getProperty("ldap.password");
//        String ldapUserDnPattern = this.environment.getProperty("ldap.user.dn.pattern");
//
//        logger.info("url: " + url);
//        logger.info("baseBn: " + baseBn);
//        logger.info("ldapSecurityPrincipal: " + ldapSecurityPrincipal);
//        logger.info("ldapPrincipalPassword: " + ldapPrincipalPassword);
//        logger.info("ldapUserDnPattern: " + ldapUserDnPattern);
//
//        auth
//                .ldapAuthentication()
//                .userDnPatterns(ldapUserDnPattern)
////                .userSearchBase("dc=demo1,dc=freeipa,dc=org")
////                .userSearchBase("dc=example,dc=com")
//                    .userSearchFilter("(&(objectclass=*)(uid=%uid))")
////                    .groupSearchBase("dc=example,dc=com")
////                    .groupSearchFilter("(objectclass=groupOfNames)")
//                .contextSource()
//                    .url(url + "/" + baseBn)
//                    .managerDn(ldapSecurityPrincipal)
//                    .managerPassword(ldapPrincipalPassword)
////                    .and()
////                .passwordCompare()
////                .passwordEncoder(new LdapShaPasswordEncoder())
////                .passwordAttribute("userPassword")
//                        ;
//
////
////        auth.inMemoryAuthentication()
////                .withUser("user").password("password").roles("USER")
////                .and()
////                .withUser("admin").password("password").roles("USER", "API", "ADMIN");
//    }


}