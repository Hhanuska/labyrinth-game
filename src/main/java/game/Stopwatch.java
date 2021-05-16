package game;

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

public class Stopwatch {

    private LongProperty millis = new SimpleLongProperty();
    private StringProperty mmssSS = new SimpleStringProperty();

    private Timeline timeline;

    public Stopwatch() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            millis.set(millis.get() + 1);
        }), new KeyFrame(Duration.millis(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        mmssSS.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(millis.get(), "mm:ss:SS"), millis));
    }

    public LongProperty millisProperty() {
        return millis;
    }

    public StringProperty mmssSSProperty() {
        return mmssSS;
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.pause();
    }

    public void restart() {
        stop();

        reset();

        start();
    }

    public void reset() {
        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }
        millis.set(0);
    }

    public Animation.Status getStatus() {
        return timeline.getStatus();
    }

}
