package com.apzda.cloud.uc.security.token;

import com.apzda.cloud.gsvc.context.TenantManager;
import com.apzda.cloud.gsvc.security.token.SimpleJwtToken;
import com.apzda.cloud.uc.test.TestBase;
import com.apzda.cloud.uc.token.UserToken;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = "classpath:/admin_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "classpath:/user_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "classpath:/tenant_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class TokenCustomizerTest extends TestBase {

    @Autowired
    private TokenCustomizer tokenCustomizer;

    @Autowired
    @Qualifier("defaultAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;

    @Test
    void customize_should_be_ok() {
        // given
        val token = UsernamePasswordAuthenticationToken.unauthenticated("gsvc", "123456");

        // when
        val authed = authenticationProvider.authenticate(token);
        val customizedToken = (UserToken) tokenCustomizer.customize(authed,
                SimpleJwtToken.builder().name("gsvc").build());
        assertThat(customizedToken).isNotNull();
        assertThat(customizedToken.getTenant()).isNotNull();
        assertThat(customizedToken.getTenant().getId()).isEqualTo("1");
        assertThat(customizedToken.getTenant().getOrganizations()).isNull();
        assertThat(customizedToken.getOrganization()).isNotNull();
        assertThat(customizedToken.getOrganization().getName()).isEqualTo("O1");
        assertThat(customizedToken.getDepartment()).isNotNull();
        assertThat(customizedToken.getDepartment().getName()).isEqualTo("D1-1");
        assertThat(customizedToken.getJob()).isNotNull();
        assertThat(customizedToken.getJob().getLevel()).isEqualTo(1);
        assertThat(customizedToken.getTenants()).isNotEmpty();
        assertThat(customizedToken.getTenants().size()).isEqualTo(2);
    }

    @Test
    void tenant_ids_should_be_tow() {
        // given
        val user = UsernamePasswordAuthenticationToken.unauthenticated("gsvc", "123456");
        val authentication = authenticationProvider.authenticate(user);
        val emptyContext = SecurityContextHolder.getContext();
        emptyContext.setAuthentication(authentication);
        // when
        val tenantId = TenantManager.tenantId();
        val ids = TenantManager.tenantIds();
        // then
        assertThat(tenantId).isEqualTo("1");
        assertThat(ids.length).isEqualTo(2);
        assertThat(ids[0]).isEqualTo("1");
        assertThat(ids[1]).isEqualTo("2");
    }

}
