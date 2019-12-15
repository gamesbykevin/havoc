package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.gamesbykevin.havoc.MyGdxGame.FRAME_MS;
import static com.gamesbykevin.havoc.decals.Wall.*;

public class Door extends DecalCustom {

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

    //how much time passed?
    private float lapsed;

    //how long does the door stay open
    private static final float DOOR_OPEN_TIME = 3500f;

    //how close do we need to be to hear the sound effect
    public static final float DOOR_DISTANCE_SFX_RATIO = 0.5f;

    //is this door a secret
    private boolean secret;

    //was this door ever opened?
    private boolean once;

    protected Door(TextureRegion texture, int side, boolean secret) {
        super(texture, Type.Door, side, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        setSecret(secret);
        setState(State.Closed);
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isOnce() {
        return this.once;
    }

    public void setOnce(boolean once) {
        this.once = once;
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

    private float getLapsed() {
        return this.lapsed;
    }

    public void setLapsed(float lapsed) {
        this.lapsed = lapsed;
    }

    @Override
    public void update() {

        switch (getState()) {

            case Start:
                setLapsed(0);
                setState(State.Opening);
                break;

            case Opening:
                switch (getSide()) {
                    case SIDE_EAST:
                    case SIDE_WEST:

                        //slide door open
                        getDecal().translate(0, -DOOR_VELOCITY, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().y < getDestination()) {
                            getDecal().getPosition().y = getDestination();
                            setLapsed(0);
                            setState(State.Open);
                        }
                        break;

                    case SIDE_NORTH:
                    case SIDE_SOUTH:

                        //slide door open
                        getDecal().translate(-DOOR_VELOCITY, 0, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().x < getDestination()) {
                            getDecal().getPosition().x = getDestination();
                            setLapsed(0);
                            setState(State.Open);
                        }
                        break;
                }
                break;

            case Open:

                //keep track of time
                setLapsed(getLapsed() + FRAME_MS);

                //if the door was open for long enough we can start to close it
                if (getLapsed() >= DOOR_OPEN_TIME)
                    setState(State.Closing);
                break;

            case Closing:

                switch (getSide()) {
                    case SIDE_EAST:
                    case SIDE_WEST:

                        //slide door open
                        getDecal().translate(0, DOOR_VELOCITY, 0);

                        //door is open all the way now
                        if (getDecal().getPosition().y >= getStart()) {
                            getDecal().getPosition().y = getStart();
                            setState(State.Closed);
                        }
                        break;

                    case SIDE_NORTH:
                    case SIDE_SOUTH:

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

                //flag that the door was opened at least once if it is a secret
                if (isSecret())
                    setOnce(true);
                break;
        }
    }
}