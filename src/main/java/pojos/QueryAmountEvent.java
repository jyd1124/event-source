package pojos;

import akka.actor.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class QueryAmountEvent implements AccountEvent, CborSerializable {

    private static final long serialVersionUID = 1L;

    private String toAccountId;

    private ActorRef toActor;

    @JsonCreator
    QueryAmountEvent(@JsonProperty("toAccountId") String toAccountId) {
        this.toAccountId = toAccountId;
    }

}
