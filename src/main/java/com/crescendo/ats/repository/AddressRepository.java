package com.crescendo.ats.repository;

import com.crescendo.ats.domain.Address;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Address entity.
 */
public interface AddressRepository extends JpaRepository<Address,Long> {

}
