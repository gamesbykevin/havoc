package com.gamesbykevin.havoc.decals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

//creating my own group strategy and shader to resolve transparency issue
public class MyGroupStrategy extends CameraGroupStrategy {

    private ShaderProgram shader;

    public MyGroupStrategy(Camera camera) {
        super(camera);
        getShader();
    }

    @Override
    public ShaderProgram getGroupShader(int group) {
        return getShader();
    }

    private ShaderProgram getShader () {

        if (this.shader == null) {
            String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                    + "uniform mat4 u_projectionViewMatrix;\n" //
                    + "varying vec4 v_color;\n" //
                    + "varying vec2 v_texCoords;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                    + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "}\n";

            String fragmentShader = "#ifdef GL_ES\n" //
                    + "precision mediump float;\n" //
                    + "#endif\n" //
                    + "varying vec4 v_color;\n" //
                    + "varying vec2 v_texCoords;\n" //
                    + "uniform sampler2D u_texture;\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + "vec4 texel = v_color * texture2D(u_texture, v_texCoords);\n"
                    + "if(texel.a < 0.01)\n"
                    + "discard;\n"
                    + "gl_FragColor = texel;\n"
                    + "}";

            this.shader = new ShaderProgram(vertexShader, fragmentShader);

            //throw exception if issue creating shader
            if (this.shader.isCompiled() == false)
                throw new IllegalArgumentException("couldn't compile shader: " + this.shader.getLog());
        }

        return this.shader;
    }

    @Override
    public void beforeGroups () {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        getShader().begin();
        getShader().setUniformMatrix("u_projectionViewMatrix", getCamera().combined);
        getShader().setUniformi("u_texture", 0);
    }

    @Override
    public void afterGroups () {
        getShader().end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    @Override
    public void dispose () {
        if (this.shader != null)
            this.shader.dispose();

        this.shader = null;
    }
}