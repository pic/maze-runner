package com.simplificator.gdx;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public class CameraHandler {

    private float moveSpeed = 3; // m/s
    private float rotSpeed = 90; // deg/s

    private final PerspectiveCamera mCamera;
    private long mFrameMs;
    private double currentBearing = Math.PI;//(float) (Math.PI/2);
    private double currentTilt = 0;

    private float deltaMoveSpeed = 0;
    private float deltaRotationSpeed = 0;

    public CameraHandler(PerspectiveCamera camera) {
        this.mCamera = camera;
        camera.position.y=1.6f; // height of the eyes
    }

    public void setFrameDelta(long frameMS) {
        this.mFrameMs = frameMS;
        deltaMoveSpeed = moveSpeed / 1000f * mFrameMs;
        deltaRotationSpeed = (float) (Math.toRadians(rotSpeed) / 1000f * mFrameMs);
    }

    public void lookLeft() {
        currentBearing += deltaRotationSpeed;
    }

    public void lookRight() {
        currentBearing -= deltaRotationSpeed;
    }

    public void lookUp() {
        currentTilt += deltaRotationSpeed;
    }

    public void lookDown() {
        currentTilt -= deltaRotationSpeed;
    }


    public void moveForward() {
        mCamera.position.x += (float) Math.sin(currentBearing) * deltaMoveSpeed;
        mCamera.position.z += (float) Math.cos(currentBearing) * deltaMoveSpeed;
    }

    public void moveBack() {
        mCamera.position.x -= (float) Math.sin(currentBearing) * deltaMoveSpeed;
        mCamera.position.z -= (float) Math.cos(currentBearing) * deltaMoveSpeed;
    }

    public void update() {
        mCamera.direction.x = (float) Math.sin(currentBearing);
        mCamera.direction.z = (float) Math.cos(currentBearing);
        mCamera.direction.y = (float) Math.sin(currentTilt);
        mCamera.update();
    }

}
