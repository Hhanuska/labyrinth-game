package game.UI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Class responsible for measuring time.
 */
public class Stopwatch {

    private LongProperty millis = new SimpleLongProperty();
    private StringProperty mmssSS = new SimpleStringProperty();

    private Timeline timeline;

    /**
     * Creates a Stopwatch object.
     */
    public Stopwatch() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            millis.set(millis.get() + 1);
        }), new KeyFrame(Duration.millis(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        mmssSS.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(millis.get(), "mm:ss.SS"), millis));
    }

    /**
     * Returns the time on the stopwatch in ms.
     *
     * @return The time on the stopwatch in ms
     */
    public LongProperty millisProperty() {
        return millis;
    }

    /**
     * Returns the time on the stopwatch as mm:ss:SSS.
     *
     * @return time on the stopwatch as mm:ss:SSS
     */
    public StringProperty mmssSSProperty() {
        return mmssSS;
    }

    /**
     * Starts the stopwatch.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the stopwatch.
     */
    public void stop() {
        timeline.pause();
    }

    /**
     * Resets the stopwatch.
     */
    public void reset() {
        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }
        millis.set(0);
    }
}
