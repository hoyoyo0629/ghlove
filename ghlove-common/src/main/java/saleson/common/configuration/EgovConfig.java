package saleson.common.configuration;

import org.egovframe.rte.fdl.cmmn.trace.LeaveaTrace;
import org.egovframe.rte.fdl.cmmn.trace.handler.DefaultTraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.handler.TraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager;
import org.egovframe.rte.fdl.cmmn.trace.manager.TraceHandlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class EgovConfig {

    @Bean
    LeaveaTrace leaveaTrace(DefaultTraceHandleManager traceHandleManager) {
        LeaveaTrace trace = new LeaveaTrace();
        trace.setTraceHandlerServices(new TraceHandlerService[]{traceHandleManager});
        return trace;
    }

    @Bean
    DefaultTraceHandleManager traceHandlerService(AntPathMatcher antPathMatcher, DefaultTraceHandler defaultTraceHandler) {
        DefaultTraceHandleManager manager = new DefaultTraceHandleManager();
        manager.setReqExpMatcher(antPathMatcher);
        manager.setPatterns(new String[]{"*"});
        manager.setHandlers(new TraceHandler[]{defaultTraceHandler});
        return manager;
    }

    @Bean
    AntPathMatcher antPathMatcher() {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher;
    }

    @Bean
    DefaultTraceHandler defaultTraceHandler() {
        DefaultTraceHandler handler = new DefaultTraceHandler();
        return handler;
    }
}
