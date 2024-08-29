package com.apzda.cloud.uc.service;

import com.apzda.cloud.uc.proto.Request;
import com.apzda.cloud.uc.proto.UcenterService;
import com.apzda.cloud.uc.test.JpaTestBase;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Sql("classpath:/admin_test.sql")
class UcenterServiceImplTest extends JpaTestBase {

    @Autowired
    private UcenterService ucenterService;

    @Test
    void authorities_should_be_loaded_correctly() {
        val authorities = ucenterService
            .getAuthorities(Request.newBuilder().setUsername("admin").setTenantId("1").build());
        assertThat(authorities).isNotNull();
        assertThat(authorities.getAuthorityCount()).isEqualTo(6);
    }

}
