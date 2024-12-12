package mk.ukim.finki.ib.authentication.web.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.service.SessionService;

import java.io.IOException;

@WebFilter(filterName = "auth-filter", urlPatterns = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD},
        initParams = {
                @WebInitParam(name = "ignore-path", value = "/login"),
                @WebInitParam(name = "register-path", value = "/register")
        })
public class SessionFilter implements Filter {
    private String ignorePath;
    private String registerPath;
    private final SessionService sessionService;

    public SessionFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        ignorePath = filterConfig.getInitParameter("ignore-path");
        registerPath = filterConfig.getInitParameter("register-path");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        System.out.println("SessionFilter: Checking path = " + path);


        if (path.startsWith(ignorePath) || path.startsWith(registerPath)|| path.startsWith("/style") || path.startsWith("/script")) {
            filterChain.doFilter(request, response);
            return;
        } else {
            Cookie[] cookieList = req.getCookies();
            if (cookieList != null) {
                for (Cookie cookie : cookieList) {
                    if (cookie.getName().equals("SESSIONID")) {
                        String sessionToken = cookie.getValue();
                        if (this.sessionService.isSessionValid(sessionToken)) {
                            filterChain.doFilter(request, response); // Valid session, proceed
                            return;
                        }
                    }
                }
            }
            resp.sendRedirect("/login");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
