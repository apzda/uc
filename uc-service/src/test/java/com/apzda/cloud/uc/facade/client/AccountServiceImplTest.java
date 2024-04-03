package com.apzda.cloud.uc.facade.client;

import com.apzda.cloud.uc.test.TestApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@SpringBootTest
@ContextConfiguration(classes = TestApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class AccountServiceImplTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void getUserInfo() {
        // given
        System.out.println("passwordEncoder.encode(\"123456\") = " + passwordEncoder.encode("123456"));
    }

}
