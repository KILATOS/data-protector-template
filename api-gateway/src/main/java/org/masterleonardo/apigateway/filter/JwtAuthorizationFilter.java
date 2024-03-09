package org.masterleonardo.apigateway.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class JwtAuthorizationFilter extends AbstractGatewayFilterFactory<JwtAuthorizationFilter.Config> {
    @Autowired
    private Environment environment;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter() {
        super(Config.class);
    }



    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            if (!serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                logger.error("Attempt of authorization without token");
                return onError(exchange,"No auth header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.substring(7);
            try{
                Map<String, Claim> claims = isJwtValid(jwt);
            } catch (JWTVerificationException e){
                logger.error("Attempt of authorization with invalid token " + jwt );
                return onError(exchange,"Invalid JWT", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    private Map<String, Claim> isJwtValid(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt.secretkey")))
                .withSubject("UserDetails")
                .withIssuer("user-api")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    public static class Config{
        //
    }
}
