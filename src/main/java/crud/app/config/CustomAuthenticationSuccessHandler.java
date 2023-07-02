package crud.app.config;

import crud.app.security.MemberDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Component
@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication)
            throws RuntimeException {
        HttpSession session = httpServletRequest.getSession();
        MemberDetails authUser = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("username", authUser.getUsername());
        // set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                try {
                    httpServletResponse.sendRedirect("/library");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                try {
                    httpServletResponse.sendRedirect("/user");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}