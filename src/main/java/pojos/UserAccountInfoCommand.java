package pojos;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserAccountInfoCommand implements CborSerializable, AccountCommand {

    private static final long serialVersionUID = 1L;

    private Long initAmount;

    private Long userId;

    private String toAccountId;

    private ActorRef<AccountResult> toActor;

    @JsonCreator
    public UserAccountInfoCommand(@JsonProperty("initAmount") Long initAmount, @JsonProperty("userId") Long userId) {
        this.initAmount = initAmount;
        this.userId = userId;
    }
}
