package io.bootique.undertow.test.junit;

import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.test.junit.BQDaemonTestFactory;
import io.bootique.test.junit.BQRuntimeDaemon;
import io.bootique.undertow.UndertowModule;
import io.bootique.undertow.UndertowServer;
import io.bootique.undertow.command.ServerCommand;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Map;
import java.util.function.Function;

/**
 * A integration test helper that starts a Bootique Undertow server.
 * <p>
 * Instances should be annotated within the unit tests with {@link Rule} or
 * {@link ClassRule}. E.g.:
 * <p>
 * <pre>
 * public class MyTest {
 *
 * 	&#64;Rule
 * 	public UndertowTestFactory testFactory = new UndertowTestFactory();
 * }
 * </pre>
 *
 * @since 0.1
 * @deprecated since 0.25 use faster generic test factory:
 * <code>BQTestFactory.app("-s").autoLoadModule().createRuntime().run()</code> or
 * <code>BQTestFactory.app("-s").autoLoadModule().run()</code>
 */
@Deprecated
public class UndertowTestFactory extends BQDaemonTestFactory {
    /**
     * @return a new instance of builder for the test runtime stack.
     * @since 0.1
     */
    @Override
    public Builder app(String... args) {
        Function<BQRuntime, Boolean> startupCheck = r -> r.getInstance(UndertowServer.class).isStarted();

        return new Builder(runtimes, args)
                .startupCheck(startupCheck)
                .modules(UndertowModule.class);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return super.apply(base, description);
    }

    public static class Builder extends io.bootique.test.junit.BQDaemonTestFactory.Builder<Builder> {

        protected Builder(Map<BQRuntime, BQRuntimeDaemon> runtimes, String[] args) {
            super(runtimes, args);
        }

        @Override
        public BQRuntime createRuntime() {
            module(binder -> BQCoreModule.extend(binder).setDefaultCommand(ServerCommand.class));
            return super.createRuntime();
        }
    }
}
