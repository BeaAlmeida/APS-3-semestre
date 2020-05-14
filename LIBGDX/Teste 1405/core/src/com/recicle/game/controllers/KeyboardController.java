package com.recicle.game.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class KeyboardController implements InputProcessor {

    public boolean left,right,up,down;
    public boolean isMouse1Down, isMouse2Down,isMouse3Down;
    //1- left 2- right 3- middle
    public boolean isDragged;
    public Vector2 mouseLocation;

    public KeyboardController() {
        mouseLocation = new Vector2();
    }

    @Override
    public boolean keyDown(int keycode) {
        //This is activated once when a key on the keyboard is pressed down.
        boolean keyProcessed = false;

        switch(keycode) { //Our flags are set to true when a key is pressed down

            case Input.Keys.LEFT:
                left = true;
                keyProcessed = true;
                break; //So if there was no “break;” in any of the code blocks and the left key was pressed every block underneath would also be run meaning our controller would think left, right, up and down were all being pressed.

            case Input.Keys.RIGHT:
                right = true;
                keyProcessed = true;
                break;

            case Input.Keys.UP:
                up = true;
                keyProcessed = true;
                break;

            case Input.Keys.DOWN:
                down = true;
                keyProcessed = true;
                break;
        }

        return keyProcessed;

    }


    @Override
    public boolean keyUp(int keycode) {
        //This as activated once when the key that was pressed down is released.
        boolean keyProcessed = false;

        switch(keycode) { //Our flags are set to false when a key is released

            case Input.Keys.LEFT:
                left = false;
                keyProcessed = true;
                break;

            case Input.Keys.RIGHT:
                right = false;
                keyProcessed = true;
                break;

            case Input.Keys.UP:
                up = false;
                keyProcessed = true;
                break;

            case Input.Keys.DOWN:
                down = false;
                keyProcessed = true;
                break;
        }

        return keyProcessed;
    }

    @Override
    public boolean keyTyped(char character) {
        //This is activated everytime the keyboard sends a character. Unlike KeyUp and KeyDown this will be called many times while the key is down.
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Called when a mouse button or finger(on phone) is pressed down.
        if(button == 0){
            isMouse1Down = true;
        }else if(button == 1){
            isMouse2Down = true;
        }else if(button == 2){
            isMouse3Down = true;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Called when a finger or mouse button is released.
        isDragged = false;
        System.out.println(button);

        if(button == 0) {
            isMouse1Down = false;
        }
        else if(button == 1) {
            isMouse2Down = false;
        }
        else if(button == 2) {
            isMouse3Down = false;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;

        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Called each time the finger or mouse moves while in the down state.
        isDragged = true;

        mouseLocation.x = screenX;
        mouseLocation.y = screenY;

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //Called when the mouse moves whether a button is pressed or not.
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        //alled when a mouse scroll wheel is moved.
        return false;
    }
}
