package org.masterleonardo.apigateway.filter;

import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
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

@Component
public class JwtAuthorizationFilter extends AbstractGatewayFilterFactory<JwtAuthorizationFilter.Config> {
    @Autowired
    private Environment environment;

    public JwtAuthorizationFilter() {
        super(Config.class);
    }



    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            if (!serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange,"No auth header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.substring(7);
            if (!isJwtValid(jwt)){
                return onError(exchange,"JWT is invalid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    private boolean isJwtValid(String jwt){
        String subject = Jwts.parser()
                .setSigningKey(environment.getProperty("jwt.secret.token"))
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
        if (subject == null || subject.isEmpty()){
            return false;
        }
        return true;
    }

    public static class Config{
        //
    }
}
