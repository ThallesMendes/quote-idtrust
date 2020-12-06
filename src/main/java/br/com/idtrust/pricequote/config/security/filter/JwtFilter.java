package br.com.idtrust.pricequote.config.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtFilter extends BasicAuthenticationFilter {

  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";


  private String jwtSecretKey;

  public JwtFilter(final AuthenticationManager authenticationManager,
                   final String jwtSecretKey) {
    super(authenticationManager);
    this.jwtSecretKey = jwtSecretKey;
  }

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request, final HttpServletResponse response,
      final FilterChain chain) throws IOException, ServletException {

    final var header = request.getHeader(HEADER_STRING);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    final var authentication = this.getAuthentication(request);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(
      final HttpServletRequest request) throws UnsupportedEncodingException {

    final var token = request.getHeader(HEADER_STRING);

    if (token != null) {
      final Claims data = Jwts.parser()
          .setSigningKey(jwtSecretKey.getBytes(StandardCharsets.UTF_8.displayName()))
          .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
          .getBody();

      if (data != null) {
        return new UsernamePasswordAuthenticationToken(data, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }

}
