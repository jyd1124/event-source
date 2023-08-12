package pojos;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import interfaces.AccountCommand;
import interfaces.CborSerializable;
import lombok.*;

/**
 * @author jyd1124
 * @date 2023/7/23 13:45
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryAmountCommand implements CborSerializable, AccountCommand {

    private static final long serialVersionUID = 1L;

    private String toAccountId;

    private ActorRef<AccountResult> toActor;

    @JsonCreator
    public QueryAmountCommand(@JsonProperty("toAccountId") String toAccountId) {
        this.toAccountId = toAccountId;
    }
}
