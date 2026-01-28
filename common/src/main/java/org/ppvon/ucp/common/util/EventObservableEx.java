package org.ppvon.ucp.common.util;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.reactive.EventObservable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.function.Consumer;

/**
 * Extension of Cobblemon's {@link EventObservable} with a java-friendly subscription hook
 * @param <T> Event type
 */
public class EventObservableEx<T> extends EventObservable<T> {

    public void on(Priority priority, Consumer<? super T> listener) {
        this.subscribe(priority, (Function1<T, Unit>) v -> {
            listener.accept(v);
            return Unit.INSTANCE;
        });
    }

    public void on(Consumer<? super T> listener) {
        on(Priority.NORMAL, listener);
    }
}
