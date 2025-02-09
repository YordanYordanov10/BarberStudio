//package bg.softuni.barberstudio.Security;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//public class SessionCheckInterceptor implements HandlerInterceptor {
//
//    private final Set<String> UNAUTHENTICATED_ENDPOINTS  = Set.of("/","/login","/register");
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        String endPoint = request.getServletPath();
//
//        if(UNAUTHENTICATED_ENDPOINTS.contains(endPoint)) {
//            return true;
//        }
//
//        HttpSession currentUserSession = request.getSession(false);
//        if (currentUserSession == null) {
//            response.sendRedirect("/login");
//            return false;
//        }
//
//
//        return true;
//    }
//}
