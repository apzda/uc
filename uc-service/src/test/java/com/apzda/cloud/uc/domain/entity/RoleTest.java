package com.apzda.cloud.uc.domain.entity;

import com.apzda.cloud.uc.domain.repository.RoleRepository;
import com.apzda.cloud.uc.test.JpaTestBase;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Sql(value = "classpath:role_test.sql")
class RoleTest extends JpaTestBase {

    @Autowired
    private RoleRepository repository;

    @Test
    void allChildren() {
        // given
        val a = new Role();
        a.setId(4L);
        a.setTenantId(0L);
        a.setRole("a");
        val b = new Role();
        b.setTenantId(0L);
        b.setRole("b");
        val c = new Role();
        c.setTenantId(0L);
        c.setRole("c");
        val d = new Role();
        d.setTenantId(0L);
        d.setRole("d");
        val e = new Role();
        e.setId(8L);
        e.setTenantId(0L);
        e.setRole("e");
        // when
        val roleOpt = repository.findById(a.getId());
        // then
        assertThat(roleOpt).isPresent();
        // when
        val children = roleOpt.get().getChildren();
        val allChildren = roleOpt.get().allChildren();
        // then
        assertThat(children).isNotEmpty();
        assertThat(children).hasSize(2);
        assertThat(children).containsAll(List.of(b, c));
        assertThat(allChildren).isNotEmpty();
        assertThat(allChildren).hasSize(4);
        assertThat(allChildren).containsAll(List.of(b, c, d, e));

        // when
        val eo = repository.findById(e.getId());
        // then
        assertThat(eo).isPresent();
        assertThat(eo.get().getChildren()).isEmpty();
        assertThat(eo.get().allChildren()).isEmpty();

    }

    @Test
    void allParents() {
        // given
        val a = new Role();
        a.setId(4L);
        a.setTenantId(0L);
        a.setRole("a");
        val b = new Role();
        b.setId(5L);
        b.setTenantId(0L);
        b.setRole("b");
        val c = new Role();
        c.setId(6L);
        c.setTenantId(0L);
        c.setRole("c");
        val d = new Role();
        d.setId(7L);
        d.setTenantId(0L);
        d.setRole("d");
        val e = new Role();
        e.setId(8L);
        e.setTenantId(0L);
        e.setRole("e");
        // when
        val opt = repository.findById(e.getId());
        // then
        assertThat(opt).isPresent();
        assertThat(opt.get().getParents()).containsAll(List.of(d));
        assertThat(opt.get().allParents()).containsAll(List.of(a, b, d));

        // when
        val opt2 = repository.findById(d.getId());
        // then
        assertThat(opt2).isPresent();
        assertThat(opt2.get().getParents()).containsAll(List.of(b));
        assertThat(opt2.get().allParents()).containsAll(List.of(a, b));
    }

}
