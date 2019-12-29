package com.gamesbykevin.havoc.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.havoc.MyGdxGame;

public abstract class CustomSelectScreen extends TemplateScreen {

    //our container table
    private Table container;

    //customize our screen
    private float buttonSize;
    private int columns;
    private int padding;
    private int total;

    //how we scroll on page
    private ScrollPane scroll;

    //remember our scroll position
    private float scrollY = 0;

    public CustomSelectScreen(MyGdxGame game) {
        super(game);

        //our container table
        this.container = new Table();
        getContainer().setFillParent(true);

        //add the container to our stage
        getStage().clear();
        getStage().addActor(container);
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }

    public Table getContainer() {
        return this.container;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getButtonSize() {
        return this.buttonSize;
    }

    public void setButtonSize(float buttonSize) {
        this.buttonSize = buttonSize;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public ScrollPane getScroll() {
        return this.scroll;
    }

    protected void create() {

        //create table to contain the gui
        Table table = new Table();

        //we need to allow the user to scroll
        this.scroll = new ScrollPane(table, getSkin());
        getScroll().setFlickScroll(true);

        table.pad(getPadding()).defaults().expandX().space(getPadding());
        table.row();
        table.add(new Label(getTitleText(), getSkin())).expandX().fillX().colspan(getColumns()).top();

        //keep track of level #
        int count = 0;

        //continue until we add all our level selections to the list
        while (true) {

            //create a new row
            table.row();

            int column = 0;

            while (column < getColumns()) {

                //if we reached our limit, let's stop
                if (count >= getTotal())
                    break;

                //need final value to apply to listener
                final int index = count;

                //each button will have the # as the label
                TextButton button = new TextButton(getButtonText(index), getSkin());

                //adjust the font size accordingly
                setButtonTextFontSize(button, index);

                //each button will be the same size
                table.add(button).size(getButtonSize(), getButtonSize()).top();

                //add on click event
                button.addListener(new ClickListener() {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        //handle the click accordingly
                        handleClick(index);
                    }
                });

                //keep track what's added
                count++;

                //increase the column as well
                column++;
            }

            //if we reached our limit, let's stop
            if (count >= getTotal())
                break;
        }

        getContainer().add(getScroll()).expand().fill();
        getContainer().row().space(getPadding()).padBottom(getPadding());
    }

    //each screen will handle clicks differently
    public abstract void handleClick(int index);

    //each screen will handle text differently
    public abstract String getButtonText(int index);

    //determine how large the text is on the button
    public abstract void setButtonTextFontSize(TextButton button, int index);

    //what is the title of the screen
    public abstract String getTitleText();

    @Override
    public void show() {

        //call parent
        super.show();

        //remove anything from the container
        getContainer().clear();

        //clear the stage
        super.getStage().clear();

        //add our container table
        super.getStage().addActor(getContainer());

        //create the screen
        create();

        //make sure we are captuRing the input
        captureInput();
    }

    @Override
    public void render(float delta) {

        //clear the screen
        super.clearScreen();

        //draw the background first
        getStage().getBatch().begin();
        drawBackground(getStage().getBatch());
        getStage().getBatch().end();

        //now draw the stage
        getStage().act(Gdx.graphics.getDeltaTime());
        getStage().draw();
    }
}