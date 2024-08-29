package com.apzda.cloud.uc.service;

import com.apzda.cloud.gsvc.exception.GsvcException;
import com.apzda.cloud.uc.domain.repository.RoleRepository;
import com.apzda.cloud.uc.proto.*;
import com.apzda.cloud.uc.test.JpaTestBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.apzda.cloud.uc.ErrorCode.HAS_CHILDREN;
import static com.apzda.cloud.uc.ErrorCode.ILLEGAL_CHILD;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/
@Sql(value = "classpath:/role_test.sql")
class RoleServiceImplTest extends JpaTestBase {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void create_and_update_should_be_ok() {
        // given
        val b1 = RoleDto.newBuilder();
        b1.setName("Test Role").setRole("test1").setDescription("Test Description");
        b1.addAllChildren(List.of("4", "7"));
        b1.addAllGranted(List.of("2", "3", "4"));
        // when
        val r1 = roleService.create(b1.buildPartial());
        entityManager.clear();
        // then
        assertThat(r1.getErrCode()).isEqualTo(0);

        // given
        val rq1 = RoleQuery.newBuilder().setRole("test1").setCurrent(1).setSize(10).buildPartial();
        // when
        val res1 = roleService.list(rq1);
        // then
        assertThat(res1.getErrCode()).isEqualTo(0);
        assertThat(res1.getResultsCount()).isEqualTo(1);
        assertThat(res1.getResults(0).getName()).isEqualTo("Test Role");
        assertThat(res1.getResults(0).getDescription()).isEqualTo("Test Description");
        assertThat(res1.getResults(0).getRole()).isEqualTo("test1");
        assertThat(res1.getResults(0).getGrantedCount()).isEqualTo(3);
        assertThat(res1.getResults(0).getGrantedList().stream().map(PrivilegeVo::getId).toList())
            .containsAll(List.of("2", "3", "4"));
        assertThat(res1.getResults(0).getChildrenCount()).isEqualTo(2);
        assertThat(res1.getResults(0).getChildrenList().stream().map(Role::getId).toList())
            .containsAll(List.of("4", "7"));

        // given
        val rid = res1.getResults(0).getId();
        val b2 = RoleDto.newBuilder()
            .setId(rid)
            .setName("Test updated")
            .setDescription("Test Description updated")
            .addAllChildren(List.of("7", "6"))
            .addAllGranted(List.of("1", "3", "4"))
            .buildPartial();

        // when
        val r2 = roleService.update(b2);
        entityManager.clear();

        // then
        assertThat(r2.getErrCode()).isEqualTo(0);

        // when
        val res2 = roleService.list(rq1);
        // then
        assertThat(res2.getErrCode()).isEqualTo(0);
        assertThat(res2.getResultsCount()).isEqualTo(1);
        assertThat(res2.getResults(0).getName()).isEqualTo("Test updated");
        assertThat(res2.getResults(0).getDescription()).isEqualTo("Test Description updated");
        assertThat(res2.getResults(0).getRole()).isEqualTo("test1");
        assertThat(res2.getResults(0).getGrantedCount()).isEqualTo(3);
        assertThat(res2.getResults(0).getGrantedList().stream().map(PrivilegeVo::getId).toList())
            .containsAll(List.of("1", "3", "4"));
        assertThat(res2.getResults(0).getChildrenCount()).isEqualTo(2);
        assertThat(res2.getResults(0).getChildrenList().stream().map(Role::getId).toList())
            .containsAll(List.of("6", "7"));
        entityManager.clear();
    }

    @Test
    void illegal_child_check_self_should_be_ok() {
        // given
        val b3 = RoleDto.newBuilder()
            .setId("6")
            .setName("Test updated")
            .setDescription("Test Description updated")
            .addAllChildren(List.of("7", "6"))
            .buildPartial();

        // when
        try {
            roleService.update(b3);
        }
        catch (GsvcException e) {
            val error = e.getError();
            assertThat(error.code()).isEqualTo(ILLEGAL_CHILD);
        }
        entityManager.clear();
    }

    @Test
    void illegal_child_check_parent_should_be_ok() {
        // given
        val b3 = RoleDto.newBuilder()
            .setId("8")
            .setName("Test updated")
            .setDescription("Test Description updated")
            .addAllChildren(List.of("7"))
            .buildPartial();

        // when
        try {
            roleService.update(b3);
        }
        catch (GsvcException e) {
            val error = e.getError();
            assertThat(error.code()).isEqualTo(ILLEGAL_CHILD);
        }
        entityManager.clear();
    }

    @Test
    void illegal_child_check_grade_parents_should_be_ok() {
        // given
        val b3 = RoleDto.newBuilder()
            .setId("8")
            .setName("Test updated")
            .setDescription("Test Description updated")
            .addAllChildren(List.of("5"))
            .buildPartial();

        // when
        try {
            roleService.update(b3);
        }
        catch (GsvcException e) {
            val error = e.getError();
            assertThat(error.code()).isEqualTo(ILLEGAL_CHILD);
        }
        entityManager.flush();
    }

    @Test
    void delete_should_be_ok() {
        // given
        val b2 = RoleId.newBuilder().setId("8");
        // when
        entityManager.clear();
        val r2 = roleService.delete(b2.buildPartial());
        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(r2.getErrCode()).isEqualTo(0);

        // given
        val a = new com.apzda.cloud.uc.domain.entity.Role();
        a.setId(4L);
        a.setTenantId(0L);
        a.setRole("a");
        val b = new com.apzda.cloud.uc.domain.entity.Role();
        b.setTenantId(0L);
        b.setRole("b");
        val c = new com.apzda.cloud.uc.domain.entity.Role();
        c.setTenantId(0L);
        c.setRole("c");
        val d = new com.apzda.cloud.uc.domain.entity.Role();
        d.setTenantId(0L);
        d.setRole("d");

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
        assertThat(allChildren).hasSize(3);
        assertThat(allChildren).containsAll(List.of(b, c, d));
    }

    @Test
    void can_not_delete_not_empty_role_should_be_ok() {
        // given
        val b1 = RoleId.newBuilder().setId("4");
        // when
        try {
            roleService.delete(b1.buildPartial());
        }
        catch (GsvcException e) {
            val error = e.getError();
            assertThat(error.code()).isEqualTo(HAS_CHILDREN);
        }
    }

}
