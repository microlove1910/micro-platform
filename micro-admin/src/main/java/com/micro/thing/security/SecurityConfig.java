package com.micro.thing.security;

import com.micro.thing.mybatis.model.SysAdmin;
import com.micro.thing.mybatis.model.SysRole;
import com.micro.thing.service.SysAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    SysAdminService sysAdminService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 用户验证
        auth.userDetailsService(new SysAdminDetailsService(sysAdminService))
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/", "/index.html", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    try (PrintWriter printWriter = response.getWriter();) {
                        printWriter.write("{\"code\": 401, \"msg\":  \"登录过期，请重新登录\"}");
                        printWriter.flush();
                    }
                })
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                // 登录
                .formLogin()
                .loginProcessingUrl("/admin/login")
                .loginPage("/#/login")
                .defaultSuccessUrl("/", true)
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())
                .and()
                // 登出
                .logout()
                .logoutUrl("/admin/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    final HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate();
                    }
                    response.setContentType("application/json;charset=utf-8");
                    try (PrintWriter writer = response.getWriter()) {
                        /* json */
                        writer.write("{\"code\":  200, \"msg\":  \"注销成功\"}");
                    }
                })
                .permitAll();
    }

    public static class SysAdminDetailsService implements UserDetailsService {

        private final SysAdminService sysAdminService;

        public SysAdminDetailsService(SysAdminService sysAdminService) {
            this.sysAdminService = sysAdminService;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            final SysAdmin sysAdmin = sysAdminService.loadByUsername(username);
            if (sysAdmin == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            final List<SysRole> sysRoles = sysAdminService.loadRolesByAdminId(sysAdmin.getId());
            return new SysAdminDetails(sysAdmin, sysRoles);
        }
    }

    public static class LoginSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            SysAdminDetails sysAdminDetails = (SysAdminDetails) authentication.getPrincipal();
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("adminId", sysAdminDetails.getId());

            logger.info("用户登录成功({}=>{})", sysAdminDetails.getId(), sysAdminDetails.getUsername());

            response.setContentType("application/json;charset=utf-8");
            try (PrintWriter printWriter = response.getWriter()) {
                Integer token = sysAdminDetails.getId();
                printWriter.write("{\"code\":  200, \"msg\":  \"ok\", \"data\": {\"token\":  \"" + token + "\"}}");
            }
        }
    }

    public static class LoginFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
            try (PrintWriter printWriter = response.getWriter()) {
                printWriter.write("{\"code\":  401, \"msg\":  \"用户名或者密码错误\"}");
            }
        }
    }
}
