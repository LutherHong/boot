package com.vueboot.boot.interceptor;

import com.vueboot.boot.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

//    /**
//     * 拦截index开头的（此处专门拦截/index）拦截之后判断当前session中是否含有user，如果没有的话会重定向到/login
//     * @param request
//     * @param response
//     * @param handler
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        String contextPath = session.getServletContext().getContextPath();
//        String[] requireAuthPages  = new String[]{
//                "index",
//        };
//        String uri = request.getRequestURI();
//
//        uri = StringUtils.remove(uri,contextPath+"/");
//        String page = uri;
//
//        if(begingWith(page,requireAuthPages)){
//            User user = (User) session.getAttribute("user");
//            if (user==null){
//                response.sendRedirect("login");
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * 完善拦截访问
     * 避免前端console里放一个假的用户。就直接进入用户了
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 放行 options 请求，否则无法让前端带上自定义的 header 信息，导致 sessionID 改变，shiro 验证失败
        // 由于跨域情况下会先发出一个 options 请求试探，这个请求是不带 cookie 信息的，所以 shiro 无法获取到 sessionId，将导致认证失败。这个地方坑了我好几个小时，要不是文章早就写完了，我什么都配置好了，请求发过来 sessionId 还是老变。也怪我一直盯着后端，没仔细看前端发的是啥请求。
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }

        Subject subject = SecurityUtils.getSubject();
        // 使用 shiro 验证
        if (!subject.isAuthenticated()) {
            return false;
        }
        return true;
    }


    private boolean begingWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            if(StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
