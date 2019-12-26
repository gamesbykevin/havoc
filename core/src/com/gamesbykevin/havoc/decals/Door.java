package com.gamesbykevin.havoc.decals;

import com.gamesbykevin.havoc.animation.DecalAnimation;
import com.gamesbykevin.havoc.dungeon.Cell;
import com.gamesbykevin.havoc.util.Timer;

import static com.gamesbykevin.havoc.MyGdxGame.FRAME_MS;

public class Door extends DecalCustom {

    //how deep is the door placed
    public static final float DOOR_DEPTH = .5f;

    //the depth will be close to the walls for a secret
    public static final float SECRET_DEPTH = .075f;

    public enum State {
        Start,
        Opening,
        Open,
        Closing,
        Closed
    }

    //what is the state of the door
    private State state;

    //how fast we open / close the doors
    private static final float DOOR_VELOCITY = .0175f;

    //where we open the door to
    private float destination;

    //where do we close the door
    private float start;

    //how long does the door stay open
    private static final float DURATION_DOOR_OPEN = 3500f;

    //how close do we need to be to hear the sound effect
    public static final float DOOR_DISTANCE_SFX_RATIO = 0.5f;

    //is this door a secret
    private boolean secret;

    //was this door ever opened?
    private boolean found;

    //timer to control the door open/close etc...
    private Timer timer;

    protected Door(Side side, DecalAnimation animation, Cell cell) {
        super(side, animation);
        setSecret(cell.isSecret());
        setState(State.Closed);
    }

    public Timer getTimer() {

        if (this.timer == null)
            this.timer = new Timer(DURATION_DOOR_OPEN);

        return this.timer;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isFound() {
        return this.found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isSecret() {
        return this.secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public float getDestination() {
        return this.destination;
    }

    public void setDestination(float destination) {
        this.destination = destination;
    }

    public float getStart() {
        return this.start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public boolean isOpen() {

        //is the door open
        switch (getState()) {
            case Open:
                return true;

            default:
                return false;
        }
    }

    @Override
    public void update() {

        //update animation
        getAnimation().update();

        switch (getState()) {

            case Start:
                getTimer().reset();
                setState(State.Opening);
                break;

            case Opening:
                switch (getSide()) {
                    case East:
                    case West:

                        //slide door open
                        getDecal().translate(0, -DOOR_VELOCITY, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().y < getDestination()) {
                            getDecal().getPosition().y = getDestination();
                            getTimer().reset();
                            setState(State.Open);
                        }
                        break;

                    case North:
                    case South:

                        //slide door open
                        getDecal().translate(-DOOR_VELOCITY, 0, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().x < getDestination()) {
                            getDecal().getPosition().x = getDestination();
                            getTimer().reset();
                            setState(State.Open);
                        }
                        break;
                }
                break;

            case Open:

                //keep track of time
                getTimer().update();

                //if the door was open for long enough we can start to close it
                if (getTimer().isExpired())
                    setState(State.Closing);
                break;

            case Closing:

                switch (getSide()) {
                    case East:
                    case West:

                        //slide door open
                        getDecal().translate(0, DOOR_VELOCITY, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().y >= getStart()) {
                            getDecal().getPosition().y = getStart();
                            setState(State.Closed);
                        }
                        break;

                    case North:
                    case South:

                        //slide door open
                        getDecal().translate(DOOR_VELOCITY, 0, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().x >= getStart()) {
                            getDecal().getPosition().x = getStart();
                            setState(State.Closed);
                        }
                        break;
                }
                break;
        }
    }

    //open the door
    public void open() {

        switch (getState()) {

            case Closed:

                //flag that we can start opening the door
                setState(State.Start);

                //flag that we found the secret if the door was opened at least once
                if (isSecret())
                    setFound(true);
                break;
        }
    }

    @Override
    public void reset() {

        //call parent
        super.reset();

        //flag that the door has never been opened
        setFound(false);
    }
}