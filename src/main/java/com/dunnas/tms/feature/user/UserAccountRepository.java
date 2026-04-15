package com.dunnas.tms.feature.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsername(String username);

    List<UserAccount> findAllByRoleIgnoreCaseOrderByNameAsc(String role);

    List<UserAccount> findAllByRoleInOrderByNameAsc(Collection<String> roles);

    List<UserAccount> findDistinctByCollaboratorTicketTypesIdOrderByNameAsc(Long ticketTypeId);
}
