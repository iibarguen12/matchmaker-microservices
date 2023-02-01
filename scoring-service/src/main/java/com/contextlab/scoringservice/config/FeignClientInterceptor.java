package com.contextlab.scoringservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER="Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String authorization = request.getHeader(AUTHORIZATION_HEADER);
            if (StringUtils.isNotBlank(authorization)) {
                requestTemplate.header("Authorization", new String[]{authorization});
            }
        }
    }
}
