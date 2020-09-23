package com.example.study.config;

import com.example.study.jwt.JwtAuthenticationTokenFilter;
import com.example.study.jwt.RewriteAccessDenyFilter;
import com.example.study.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 */
@EnableWebSecurity //开启springSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//安全验证
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //自定义无权限访问拦截器
    @Autowired
    private RewriteAccessDenyFilter rewriteAccessDenyFilter;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 把MD5的密码加密装配到容器中
     *
     * @return
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return Md5Util.stringMD5(charSequence.toString());
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return Md5Util.stringMD5(charSequence.toString()).equalsIgnoreCase(s);
            }
        };
    }

    /**
     * 把密码加密装配到容器中
     *
     * @return
     */
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable() //csrf禁用
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()//授权请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //放行所有
                .antMatchers("/auth/**").permitAll()///放行所有auth 路径
                .antMatchers("/sysUser/addSysUser").permitAll()///放行所有auth 路径
                .antMatchers("/druid/**").anonymous()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()//放行actuator所有接口
                .anyRequest().authenticated()//默认其他请求都要认证
                .and().headers().cacheControl();//

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(rewriteAccessDenyFilter);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true); //允许凭证
        cors.addAllowedOrigin("*");
        cors.addAllowedHeader("*"); // 允许添加头信息
        cors.addAllowedMethod("*"); //允许添加方法
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", cors);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
