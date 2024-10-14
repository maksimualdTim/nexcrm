package uz.nexgroup.nexcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.nexgroup.nexcrm.model.Account;
import uz.nexgroup.nexcrm.model.Pipeline;

public interface PipelineRepository extends JpaRepository<Pipeline, Long>{
    public List<Pipeline> findAllByAccount(Account account);
}
