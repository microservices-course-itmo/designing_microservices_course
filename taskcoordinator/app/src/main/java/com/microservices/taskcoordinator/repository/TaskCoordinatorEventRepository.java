package com.microservices.taskcoordinator.repository;


import com.microservices.taskcoordinator.entity.TaskCoordinatorEventLogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskCoordinatorEventRepository extends CrudRepository<TaskCoordinatorEventLogEntity, Integer> {
    @Query(value = "select * from task_coordinator.taskcoordinator_events where event_status = 'PENDING' " +
            "order by created_time asc limit 1", nativeQuery = true)
    Optional<TaskCoordinatorEventLogEntity> findEldestNotSentEvent();
}
