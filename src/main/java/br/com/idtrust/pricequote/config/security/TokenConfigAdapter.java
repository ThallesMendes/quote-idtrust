package br.com.idtrust.pricequote.config.security;

import br.com.idtrust.pricequote.config.security.filter.JwtFilter;
import br.com.idtrust.pricequote.config.security.handler.HandlerAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class TokenConfigAdapter extends WebSecurityConfigurerAdapter {

  @Value("${jwt.secretKey}")
  private String jwtSecretKey;

  @Autowired
  private HandlerAuthenticationEntryPoint handlerAuthenticationEntryPoint;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
        .headers()
        .frameOptions()
        .disable()
        .and()
        .authorizeRequests()
        .antMatchers("/**")
        .authenticated()
        .and()
        .addFilter(new JwtFilter(super.authenticationManager(), this.jwtSecretKey))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(this.handlerAuthenticationEntryPoint)
        .and()
        .csrf()
        .disable();
  }


}
