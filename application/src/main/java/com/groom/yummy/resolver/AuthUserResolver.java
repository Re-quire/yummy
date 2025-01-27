//package com.groom.yummy.resolver;
//
//import com.groom.yummy.domain.user.User;
//import com.groom.yummy.domain.user.UserAuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.MethodParameter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//@RequiredArgsConstructor
//public class AuthUserResolver implements HandlerMethodArgumentResolver {
//
//    private final UserAuthService userAuthService;
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return User.class.isAssignableFrom(parameter.getParameterType());
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth == null){
//            throw new RuntimeException();
//        }
//        return userAuthService.findAuthUserByEmail(auth.getName()).orElseThrow(RuntimeException::new);
//    }
//}
