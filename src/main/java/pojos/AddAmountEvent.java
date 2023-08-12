package pojos;

import akka.actor.typed.ActorRef;
import interfaces.AccountEvent;
import interfaces.CborSerializable;
import lombok.*;

/**
 * @author jyd1124
 * @date 2023/7/23 13:45
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddAmountEvent implements AccountEvent, CborSerializable {

    private static final long serialVersionUID = 1L;

    private Long value;

    private String toAccountId;

    private ActorRef<AccountResult> toActor;

    public static AddAmountEvent convertCommand (AddAmountCommand command) {
        return AddAmountEvent
                .builder()
                .value(command.getValue())
                .toAccountId(command.getToAccountId())
                .build();
    }
}
