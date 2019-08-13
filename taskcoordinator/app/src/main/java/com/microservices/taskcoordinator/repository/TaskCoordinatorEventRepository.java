package com.microservices.taskcoordinator.repository;


import com.microservices.taskcoordinator.entity.EventStatus;
import com.microservices.taskcoordinator.entity.TaskCoordinatorEventLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskCoordinatorEventRepository extends CrudRepository<TaskCoordinatorEventLogEntity, Integer> {
    Optional<TaskCoordinatorEventLogEntity> findFirstByEventStatusOrderByCreatedTimeAsc(EventStatus eventStatus);

    default Optional<TaskCoordinatorEventLogEntity> findEldestNotSentEvent() {
        return findFirstByEventStatusOrderByCreatedTimeAsc(EventStatus.PENDING);
    }
}
