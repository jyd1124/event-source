package pojos;

import akka.actor.ActorRef;
import interfaces.AccountCommand;
import interfaces.CborSerializable;
import lombok.*;

/**
 * @author jyd1124
 * @date 2023/7/23 15:32
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LogOutAccountCommand implements CborSerializable, AccountCommand {

    private static final long serialVersionUID = 1L;

    private String toAccountId;

    private ActorRef toActor;

}
