package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.gamesbykevin.havoc.MyGdxGame.FRAME_MS;

public class Door extends DecalCustom {

    //is the door open
    private boolean open = false;

    //are we opening or closing
    private boolean opening = false;
    private boolean closing = false;

    //is the door closed
    private boolean closed = false;

    //how fast we open / close the doors
    private static final float DOOR_VELOCITY = .025f;

    //where we open the door to
    private float destination;

    //where do we close the door
    private float start;

    //how much time passed?
    private float lapsed;

    //how long does the door stay open
    private static final float DOOR_OPEN_TIME = 2500f;

    public Door(TextureRegion texture, Side side) {
        super(texture, Type.Door, side, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        setOpen(false);
        setOpening(false);
        setClosing(false);
        setClosed(true);
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

    public float getLapsed() {
        return this.lapsed;
    }

    public void setLapsed(float lapsed) {
        this.lapsed = lapsed;
    }

    @Override
    public void update() {

        if (isOpening()) {

            switch (getSide()) {
                case East:
                case West:

                    //slide door open
                    getDecal().translate(0, -DOOR_VELOCITY, 0);

                    //door is open all the way now
                    if (getDecal().getPosition().y < getDestination()) {
                        getDecal().getPosition().y = getDestination();
                        setLapsed(0);
                        setOpening(false);
                        setOpen(true);
                    }
                    break;

                case North:
                case South:

                    //slide door open
                    getDecal().translate(-DOOR_VELOCITY, 0, 0);

                    //door is open all the way now
                    if (getDecal().getPosition().x < getDestination()) {
                        getDecal().getPosition().x = getDestination();
                        setLapsed(0);
                        setOpening(false);
                        setOpen(true);
                    }
                    break;
            }

        } else if (isOpen()) {

            //keep track of time
            setLapsed(getLapsed() + FRAME_MS);

            //if the door was open for long enough we can start to close it
            if (getLapsed() >= DOOR_OPEN_TIME) {
                setOpen(false);
                setClosing(true);
            }

        } else if (isClosing()) {

            switch (getSide()) {
                case East:
                case West:

                    //slide door open
                    getDecal().translate(0, DOOR_VELOCITY, 0);

                    //door is open all the way now
                    if (getDecal().getPosition().y >= getStart()) {
                        getDecal().getPosition().y = getStart();
                        setClosing(false);
                        setClosed(true);
                    }
                    break;

                case North:
                case South:

                    //slide door open
                    getDecal().translate(DOOR_VELOCITY, 0, 0);

                    //door is open all the way now
                    if (getDecal().getPosition().x >= getStart()) {
                        getDecal().getPosition().x = getStart();
                        setClosing(false);
                        setClosed(true);
                    }
                    break;
            }

        }
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isOpening() {
        return this.opening;
    }

    public void setOpening(boolean opening) {
        this.opening = opening;
    }

    public boolean isClosing() {
        return this.closing;
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
    }
}