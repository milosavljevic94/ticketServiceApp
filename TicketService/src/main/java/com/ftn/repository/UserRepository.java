package com.ftn.repository;

import com.ftn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);
	
	User findByConfirmationToken(String confirmationToken);

	User findByUsernameOrEmail(String userName, String email);

}
