package com.ecommerce.repository;

import com.ecommerce.entity.Profile;
import com.ecommerce.entity.User;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,String> {
    Profile findByUser(User user);

}
