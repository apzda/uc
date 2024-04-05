package com.apzda.cloud.uc.context;

import com.apzda.cloud.gsvc.dto.CurrentUser;
import com.apzda.cloud.gsvc.ext.GsvcExt;
import com.apzda.cloud.gsvc.security.config.GsvcSecurityAutoConfiguration;
import com.apzda.cloud.uc.TestApp;
import com.apzda.cloud.uc.autoconfig.UCenterAutoConfiguration;
import com.apzda.cloud.uc.mapper.CurrentUserMapper;
import com.apzda.cloud.uc.proto.Request;
import com.apzda.cloud.uc.proto.UcenterService;
import com.apzda.cloud.uc.proto.UserInfo;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/

@SpringBootTest
@ContextConfiguration(classes = TestApp.class)
@ImportAutoConfiguration({ UCenterAutoConfiguration.class, GsvcSecurityAutoConfiguration.class,
        SecurityAutoConfiguration.class })
@ActiveProfiles("test")
class UCenterTenantManagerTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CurrentUserMapper currentUserMapper;

    @MockBean
    private UcenterService ucenterService;

    @Test
    void should_be_ok() {
        // given
        val builder = UserInfo.newBuilder();
        builder.setErrCode(0);
        builder.setEnabled(true);
        builder.setUsername("gsvc");
        builder.setPassword("123456");
        builder.setUid("gsvc");
        builder.setAccountNonLocked(true);
        builder.setAccountNonExpired(true);
        builder.setCredentialsNonExpired(true);
        given(ucenterService.getUserInfo(any(Request.class))).willReturn(builder.build());

        // when
        val user = userDetailsService.loadUserByUsername("gsvc");

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("gsvc");
        // assertThat(user.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    void msg_2_dto_should_be_ok() {
        // given
        val builder = GsvcExt.CurrentUser.newBuilder();
        builder.setUid("123");
        builder.setApp("gsvc");
        // when
        val user = currentUserMapper.from(builder.build());

        // then
        assertThat(user.getUid()).isEqualTo("123");
        assertThat(user.getApp()).isEqualTo("gsvc");
        assertThat(user.getDevice()).isNull();
    }

    @Test
    void dto_2_msg_should_be_ok() {
        // given
        val builder = CurrentUser.builder();
        builder.uid("123");
        builder.app("gsvc");
        // when
        val user = currentUserMapper.from(builder.build());

        // then
        assertThat(user.getUid()).isEqualTo("123");
        assertThat(user.getApp()).isEqualTo("gsvc");
        assertThat(user.hasDevice()).isFalse();
    }

}
