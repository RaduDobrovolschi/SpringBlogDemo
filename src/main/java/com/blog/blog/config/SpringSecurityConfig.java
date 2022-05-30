package com.blog.blog.config;

import com.blog.blog.BlogApplication;
import com.blog.blog.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(BlogApplication.class);

    @Autowired
    @Qualifier("datasource")
    private DataSource dataSource;

    public SpringSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/api/users/**").hasAuthority("admin")
                .anyRequest().permitAll()//.authenticated()
                .and().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .authoritiesByUsernameQuery("select username, role.role_name FROM \"user\" inner join user_role role on \"user\".id = role.user_id where username = ?")
            .usersByUsernameQuery("select username, password, true FROM \"user\" where username = ?")
            .passwordEncoder(new BCryptPasswordEncoder());
    }
}