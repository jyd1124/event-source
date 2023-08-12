package actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import routers.AccountRouters;

/**
 * @author jyd1124
 * @date 2023/7/27 8:46
 */
public final class Guardian {

    public static Behavior<Void> create(int httpPort) {
        return Behaviors.setup(context -> {
            AccountActorPersist.initSharding(context.getSystem());
            AccountRouters routes = new AccountRouters(context.getSystem());
            AccountHttpServer.start(routes.createRouter(), httpPort, context.getSystem());

            return Behaviors.empty();
        });
    }
}
