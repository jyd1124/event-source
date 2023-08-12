package pojos;

import akka.actor.ActorRef;
import interfaces.CborSerializable;
import lombok.*;

/**
 * @author jyd1124
 * @date 2023/7/23 13:35
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransferEvent implements CborSerializable {

    private static final long serialVersionUID = 1L;

    private String fromAccountId;

    private String toAccountId;

    private Long value;

    private ActorRef toActor;
}
