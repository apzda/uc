package com.apzda.cloud.uc.facade.client;

import com.apzda.cloud.uc.domain.service.UserManager;
import com.apzda.cloud.uc.server.EnableUCenterServer;
import com.apzda.cloud.uc.test.TestApp;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@EnableUCenterServer
@SpringBootTest
@ContextConfiguration(classes = TestApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class AccountServiceImplTest {

    @Autowired
    private UserManager userManager;

    @Test
    void getUserInfo() {
        // given
        String username = "admin";
        // when
        val user = userManager.getUserByUsername(username);
        // then
        assertThat(user).isNotNull();
    }

}
