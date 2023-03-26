package com.movierating;

import com.movierating.helpers.JwtTokenUtil;
import com.movierating.helpers.UriHelper;
import com.movierating.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        Cookie authCookie = WebUtils.getCookie(request, "flix_auth_token");
        if (authCookie == null) {
            response.reset();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            String value = jwtTokenUtil.decodeToken(authCookie.getValue());
            int userId = Integer.parseInt(value);
            if (!userService.userExists(userId)) {
                throw new IllegalArgumentException("No such user exists with id: " + userId);
            }
            request.setAttribute("user_id", userId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.reset();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return UriHelper.isPublic(request.getServletPath());
    }
}