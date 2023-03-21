package com.aibiigae1221.cookcook;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.aibiigae1221.cookcook.security.CustomUserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class SecurityConfig {
	@Value("${jwt.public.key}")
	RSAPublicKey pubKey;

	@Value("${jwt.private.key}")
	RSAPrivateKey privKey;
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeHttpRequests((authorize) -> 
						authorize
							.requestMatchers("/sign-up", "/login").permitAll()
							.anyRequest().authenticated()
				)
				.csrf().disable()
				.httpBasic(Customizer.withDefaults())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
						.accessDeniedHandler(new BearerTokenAccessDeniedHandler())
				);

		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.pubKey).build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(this.pubKey).privateKey(this.privKey).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
	
	
}
