package routers;

import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.Route;
import interfaces.AccountCommand;
import pojos.*;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static actors.AccountActorPersist.TypeKey;
import static akka.http.javadsl.server.Directives.*;


/**
 * @author jyd1124
 * @date 2023/7/26 20:40
 */
public class AccountRouters {
    private final ClusterSharding sharding;
    private final Duration timeout;

    public AccountRouters(ActorSystem<?> system) {
        sharding = ClusterSharding.get(system);
        timeout = system.settings().config().getDuration("bankaccount.routes.ask-timeout");
    }

    private CompletionStage<AccountResult> register(UserAccountInfoCommand command) {
        String accountId = UUID.randomUUID().toString();
        EntityRef<AccountCommand> ref = sharding.entityRefFor(TypeKey, accountId);
        return ref.ask(replyTo -> new UserAccountInfoCommand(command.getInitAmount(), command.getUserId(), accountId, replyTo), timeout);
    }

    private CompletionStage<AccountResult> query(String accountId, QueryAmountCommand command) {
        EntityRef<AccountCommand> ref = sharding.entityRefFor(TypeKey, accountId);
        return ref.ask(replyTo -> new QueryAmountCommand(command.getToAccountId(), replyTo), timeout);
    }

    private CompletionStage<AccountResult> add(String accountId, AddAmountCommand command) {
        EntityRef<AccountCommand> ref = sharding.entityRefFor(TypeKey, accountId);
        return ref.ask(replyTo -> new AddAmountCommand(command.getValue(), command.getToAccountId(), replyTo), timeout);
    }

    private CompletionStage<AccountResult> sub(String accountId, SubAmountCommand command) {
        EntityRef<AccountCommand> ref = sharding.entityRefFor(TypeKey, accountId);
        return ref.ask(replyTo -> new SubAmountCommand(command.getValue(), command.getToAccountId(), replyTo), timeout);
    }

    public Route createRouter() {
        return concat(
                post(() ->
                    path("reg", () ->
                            entity(Jackson.unmarshaller(UserAccountInfoCommand.class), command ->
                                    completeOKWithFuture(register(command), Jackson.marshaller())
                        ))),
                    path("get", () ->
                            entity(Jackson.unmarshaller(QueryAmountCommand.class), command ->
                                completeOKWithFuture(query(command.getToAccountId(), command), Jackson.marshaller())
                            )),
                    path("add", () ->
                            entity(Jackson.unmarshaller(AddAmountCommand.class), command ->
                                completeOKWithFuture(add(command.getToAccountId(), command), Jackson.marshaller())
                            )),
                    path("sub", () ->
                            entity(Jackson.unmarshaller(SubAmountCommand.class), command ->
                                completeOKWithFuture(sub(command.getToAccountId(), command), Jackson.marshaller())
                            ))
        );
    }
}
