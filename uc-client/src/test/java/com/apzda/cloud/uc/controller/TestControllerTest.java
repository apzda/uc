package com.apzda.cloud.uc.controller;

import com.apzda.cloud.gsvc.client.IServiceCaller;
import com.apzda.cloud.uc.resolver.CurrentUserParamResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@WebMvcTest
@ActiveProfiles("test")
class TestControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private IServiceCaller serviceCaller;

    @Test
    @WithMockUser("gsvc")
    void ok() throws Exception {
        mvc.perform(get("/test/ok")).andExpect(status().isOk()).andExpect(content().string("gsvc"));
    }

    @Test
    void ok1() throws Exception {
        mvc.perform(get("/test/ok").accept(MediaType.TEXT_HTML_VALUE)).andExpect(status().is(302)).andExpect(header().exists("Location"));
    }

    @TestConfiguration
    static class WebMvcConfigure implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new CurrentUserParamResolver());
        }
    }
}
