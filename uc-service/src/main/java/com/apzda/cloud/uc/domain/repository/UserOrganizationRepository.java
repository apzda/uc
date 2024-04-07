package com.apzda.cloud.uc.domain.repository;

import com.apzda.cloud.uc.domain.entity.User;
import com.apzda.cloud.uc.domain.entity.UserOrganization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserOrganizationRepository
        extends PagingAndSortingRepository<UserOrganization, Long>, CrudRepository<UserOrganization, Long> {

    @Query("select u from UserOrganization u where u.user = ?1")
    List<UserOrganization> listByUser(User user);

}
