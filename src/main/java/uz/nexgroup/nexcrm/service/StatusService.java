package uz.nexgroup.nexcrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import uz.nexgroup.nexcrm.model.Status;
import uz.nexgroup.nexcrm.repository.StatusRepository;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public Status findById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Status with id: " + id + " not found"));
    }

    public List<Status> createStatus(List<Status> statuses) {
        return statusRepository.saveAll(statuses);
    }

    public Status createStatus(Status status) {
        return statusRepository.save(status);
    }

    public void deleteStatus(Long id) {
        Status status = findById(id);
        statusRepository.delete(status);
    }
}
