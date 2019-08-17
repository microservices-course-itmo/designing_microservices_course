package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.laundrymanagement.api.messages.LaundryStateWrapper.LaundryState;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InboundLaundryStateDto {

    @NotNull
    private Integer laundryId;

    @NotNull
    @Min(value = 0L, message = "Queue waiting time must be positive")
    private Long queueWaitingTime;

    @NotNull
    private Integer version;

    public InboundLaundryStateDto(LaundryState laundryStateMessage) {
        Objects.requireNonNull(laundryStateMessage);

        this.laundryId = laundryStateMessage.getLaundryId();
        this.queueWaitingTime = laundryStateMessage.getQueueWaitingTime();
        this.version = laundryStateMessage.getVersion();
    }

}
