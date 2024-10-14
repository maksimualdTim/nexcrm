package uz.nexgroup.nexcrm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import uz.nexgroup.nexcrm.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{
    Page<Contact> findAll(Pageable pageable);
}
