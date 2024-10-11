package uz.nexgroup.nexcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.nexgroup.nexcrm.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long>{
    Page<Lead> findAll(Pageable pageable);
}
