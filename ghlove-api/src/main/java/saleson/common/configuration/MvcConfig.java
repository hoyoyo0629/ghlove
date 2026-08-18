package saleson.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.board.bind.BoardContextArgumentResolver;
import com.onlinepowers.framework.web.bind.support.RequestContextArgumentResolver;
import com.onlinepowers.framework.web.bind.support.TokenCheckResultArgumentResolver;
import com.onlinepowers.framework.web.interceptor.RequestContextHandlerInterceptor;
import com.onlinepowers.framework.web.interceptor.WebLogInterceptor;
import com.onlinepowers.framework.web.servlet.handler.JsonViewResponseMethodProcessor;
import com.onlinepowers.framework.web.servlet.handler.OpHandlerExceptionResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import saleson.common.interceptor.ApiIpAuthenticationHandlerInterceptor;
import saleson.common.interceptor.SellerIpAuthenticationHandlerInterceptor;
import saleson.common.interceptor.ShopHandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		String[] excludePath = new String[]{"/content/**", "/upload"};

        registry.addInterceptor(localeChangeInterceptor())
                .addPathPatterns("/**");

        registry.addInterceptor(webLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);

        registry.addInterceptor(requestContextHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);

        registry.addInterceptor(shopHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);

		registry.addInterceptor(apiIpAuthenticationHandlerInterceptor())
				.addPathPatterns("/api/**")
				.excludePathPatterns(excludePath);

    }

    //우리가 만든 ArgumentResolver를 추가한다.
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestContextArgumentResolver());
        argumentResolvers.add(new BoardContextArgumentResolver());
        argumentResolvers.add(new TokenCheckResultArgumentResolver());
        //argumentResolvers.add(querydslPredicateArgumentResolver());
        argumentResolvers.add(pageableHandlerMethodArgumentResolver());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(jsonViewResponseMethodProcessor());
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "OPTIONS")  // "PUT", "DELETE"
                .maxAge(3600);            // pre-flight의 요청 결과를 저장할 시간 지정. 해당 시간 동안은 pre-flight를 다시 요청하지 않는다.
        ;
    }

    // querydsl
/*    @Bean
    public QuerydslBindingsFactory querydslBindingsFactory() {
        return new QuerydslBindingsFactory(new SimpleEntityPathResolver("INSTANCE"));
    }


    @Bean
    public QuerydslPredicateArgumentResolver querydslPredicateArgumentResolver() {
        return new QuerydslPredicateArgumentResolver(querydslBindingsFactory(), Optional.of(conversionService));
    }*/

    // JPA
    @Bean
    public PageRequest pageRequest() {
        return PageRequest.of(1, 10);
    }

    @Bean
    public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true);
        resolver.setFallbackPageable(pageRequest());

        return resolver;
    }


    // Locale Resolver
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREAN);
        resolver.setCookieMaxAge(100000000);

        return resolver;
    }


    // Exception Resolver
    @Bean
    public OpHandlerExceptionResolver opHandlerExceptionResolver() {
        return new OpHandlerExceptionResolver();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jacksonConverter = new
                MappingJackson2HttpMessageConverter();
        jacksonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.valueOf("application/json")));
        jacksonConverter.setObjectMapper(objectMapper);
        return jacksonConverter;
    }

    @Bean
    public JsonViewResponseMethodProcessor jsonViewResponseMethodProcessor() {
        List<HttpMessageConverter<?>> converters = Arrays.asList(mappingJackson2HttpMessageConverter());
        JsonViewResponseMethodProcessor jsonViewResponseMethodProcessor = new JsonViewResponseMethodProcessor(converters);
        return jsonViewResponseMethodProcessor;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public WebLogInterceptor webLogInterceptor() {
        return new WebLogInterceptor();
    }

    @Bean
    public RequestContextHandlerInterceptor requestContextHandlerInterceptor() {
        return new RequestContextHandlerInterceptor();
    }

    @Bean
    public ShopHandlerInterceptor shopHandlerInterceptor() {
        return new ShopHandlerInterceptor();
    }

	@Bean
	public ApiIpAuthenticationHandlerInterceptor apiIpAuthenticationHandlerInterceptor() {
		return new ApiIpAuthenticationHandlerInterceptor();
	}

}
