import actors.Guardian;
import akka.actor.AddressFromURIString;
import akka.actor.typed.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jyd1124
 * @date 2023/7/27 8:47
 */
public class AccountCluster {

    public static void main(String[] args) throws Exception {
        List<Integer> seedNodePorts =
                ConfigFactory.load().getStringList("akka.cluster.seed-nodes")
                        .stream()
                        .map(AddressFromURIString::parse)
                        .map(addr -> (Integer) addr.port().get())
                        .collect(Collectors.toList());

        List<Integer> ports = Arrays.stream(args).findFirst().map(str ->
                Collections.singletonList(Integer.parseInt(str))
        ).orElseGet(() -> {
            List<Integer> portsAndZero = new ArrayList<>(seedNodePorts);
            portsAndZero.add(0);
            return portsAndZero;
        });

        for (int port : ports) {
            final int httpPort;
            if (port > 0) httpPort = 10000 + port;  // offset from akka port
            else httpPort = 0; // let OS decide

            Config config = configWithPort(port);
            ActorSystem.create(Guardian.create(httpPort), "BankAccounts", config);
        }
    }

    private static Config configWithPort(int port) {
        return ConfigFactory.parseMap(
                Collections.singletonMap("akka.remote.artery.canonical.port", Integer.toString(port))
        ).withFallback(ConfigFactory.load());
    }

}
