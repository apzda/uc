package com.apzda.cloud.uc.domain.entity;

import com.apzda.cloud.uc.domain.repository.OrganizationRepository;
import com.apzda.cloud.uc.domain.repository.UserOrganizationRepository;
import com.apzda.cloud.uc.domain.repository.UserRepository;
import com.apzda.cloud.uc.domain.service.UserManager;
import com.apzda.cloud.uc.test.JpaTestBase;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/

@Sql("classpath:/admin_test.sql")
class UserTest extends JpaTestBase {

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @Test
    @Transactional
    void admin_user_data_is_ok() {
        // given
        val username = "admin";
        // when
        val user = userManager.getUserByUsername(username);
        // then
        assertThat(user.getRoles().size()).isEqualTo(3);
        assertThat(user.getRoles().get(0).getRole()).isEqualTo("sa");
        assertThat(user.getRoles().get(1).getRole()).isEqualTo("admin");
        assertThat(user.getMetas().size()).isEqualTo(2);

        val allRoles = user.allRoles(0L);
        assertThat(allRoles.size()).isEqualTo(3);

        val privileges = user.privileges(0L);
        assertThat(privileges.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void tenant_admin_user_data_is_ok() {
        // given
        val username = "admin";
        // when
        val user = userManager.getUserByUsername(username);
        // then
        assertThat(user.getRoles().size()).isEqualTo(3);
        assertThat(user.getRoles().get(0).getRole()).isEqualTo("sa");
        assertThat(user.getRoles().get(1).getRole()).isEqualTo("admin");
        assertThat(user.getRoles().get(2).getRole()).isEqualTo("sa");
        assertThat(user.getMetas().size()).isEqualTo(2);

        val allRoles = user.allRoles(1L);
        assertThat(allRoles.size()).isEqualTo(4);

        val privileges = user.privileges(1L);
        assertThat(privileges.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Modifying
    void grant_should_be_ok() {
        // given
        val organization = new Organization();
        organization.setName("test");
        organization.setIcon("");
        organization.setTenantId(0L);

        val org = organizationRepository.save(organization);
        assertThat(organization.getId()).isNotNull();

        // when
        val user = userManager.getUserByUsername("admin");

        val ou = new UserOrganization();
        ou.setOrganization(org);
        ou.setTenantId(org.getTenantId());
        ou.setUser(user);

        user.getOrganizations().add(ou);

        userRepository.save(user);

        val ous = userOrganizationRepository.listByUser(user);
        assertThat(ous).isNotEmpty();

        user.getOrganizations().clear();

        userRepository.save(user);
        val ous1 = userOrganizationRepository.listByUser(user);
        assertThat(ous1).isNotEmpty();
    }

}
