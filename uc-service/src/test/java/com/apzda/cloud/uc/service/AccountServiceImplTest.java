package com.apzda.cloud.uc.service;

import com.apzda.cloud.uc.proto.AccountService;
import com.apzda.cloud.uc.test.JpaTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Sql("classpath:/admin_test.sql")
class AccountServiceImplTest extends JpaTestBase {
    @Autowired
    private AccountService accountService;

    @Test
    void updatePasswordShouldBeWork() {

    }

}
