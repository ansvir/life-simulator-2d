package com.itique.ls2d.control;

public class KeyboardControl {

    private static final int ROTATION_SPEED = 20;

    private int x;
    private int y;
    private int xDelta;
    private int yDelta;
    private int rotation;
    private int speed;
    private int maxSpeed;
    private int currentSpeed;
    private int accelerator;
    private int decelerator;
    private int sprintAccelerator;
    private int sprintDecelerator;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean move45;
    private boolean move135;
    private boolean move225;
    private boolean move315;
    private boolean stop;
    private boolean sprint;

    public KeyboardControl(int x, int y, int rotation, int speed, int maxSpeed, int accelerator,
                           int decelerator) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.speed = speed;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = 0;
        this.accelerator = accelerator;
        this.decelerator = decelerator;
        this.sprintAccelerator = speed * 2;
        this.sprintDecelerator = speed * 2;
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
        this.stop = true;
        this.sprint = false;
    }

    public void updateMotion(float delta) {
        int x = this.x;
        int y = this.y;
        if (this.sprint && this.currentSpeed <= this.maxSpeed) {
            this.currentSpeed = this.speed * this.sprintAccelerator;
        }
        if (this.currentSpeed <= this.maxSpeed) {
            this.currentSpeed = this.speed * this.accelerator;
        }
        if (this.stop && this.currentSpeed > 0) {
            this.currentSpeed = this.speed / this.decelerator;
        }
        if (this.stop && this.currentSpeed > 0 && this.sprint) {
            this.currentSpeed = this.speed / this.sprintDecelerator;
        }
        if (this.up) {
            y += this.currentSpeed + delta;
            this.rotation = 270;
        }
        if (this.down) {
            y -= this.currentSpeed - delta;
            this.rotation = 90;
        }
        if (this.left) {
            x += this.currentSpeed + delta;
            this.rotation = 0;
        }
        if (this.right) {
            x -= this.currentSpeed - delta;
            this.rotation = 180;
        }
        if (this.move45) {
            x += (this.currentSpeed + delta) / 2;
            y += (this.currentSpeed + delta) / 2;
            this.rotation = 315;
        }
        if (this.move135) {
            x -= (this.currentSpeed - delta) / 2;
            y += (this.currentSpeed + delta) / 2;
            this.rotation = 225;
        }
        if (this.move225) {
            x -= (this.currentSpeed - delta) / 2;
            y -= (this.currentSpeed - delta) / 2;
            this.rotation = 135;
        }
        if (this.move315) {
            x += (this.currentSpeed + delta) / 2;
            y -= (this.currentSpeed - delta) / 2;
            this.rotation = 45;
        }
        this.xDelta = this.x - x;
        this.yDelta = this.y - y;
        this.x = x;
        this.y = y;
    }

    public void up() {
        this.up = true;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
    }

    public void down() {
        this.up = false;
        this.down = true;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
    }

    public void left() {
        this.up = false;
        this.down = false;
        this.left = true;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
    }

    public void right() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = true;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
    }

    public void move45() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = true;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
    }

    public void move135() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = true;
        this.move225 = false;
        this.move315 = false;
    }

    public void move225() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = true;
        this.move315 = false;
    }

    public void move315() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = true;
    }

    public void sprint() {
        this.sprint = true;
    }

    public void stop() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.move45 = false;
        this.move135 = false;
        this.move225 = false;
        this.move315 = false;
        this.stop = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getXDelta() {
        return xDelta;
    }

    public int getYDelta() {
        return yDelta;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isMove45() {
        return move45;
    }

    public void setMove45(boolean move45) {
        this.move45 = move45;
    }

    public boolean isMove135() {
        return move135;
    }

    public void setMove135(boolean move135) {
        this.move135 = move135;
    }

    public boolean isMove225() {
        return move225;
    }

    public void setMove225(boolean move225) {
        this.move225 = move225;
    }

    public boolean isMove315() {
        return move315;
    }

    public void setMove315(boolean move315) {
        this.move315 = move315;
    }

    public boolean isSprint() {
        return sprint;
    }

    @Deprecated(since = "Will be introduced in future releases")
    private int calculateRotation(int degrees) {
        int rotationTimes = this.rotation / 360 == 0 ? 1 : this.rotation / 360;
        this.rotation /= rotationTimes;
        int clockwise = this.rotation - degrees;
        int counterClockwise = 360 - clockwise;
        if (clockwise > counterClockwise) {
            return this.rotation - ROTATION_SPEED;
        } else if (clockwise < counterClockwise) {
            return this.rotation + ROTATION_SPEED;
        } else if (this.rotation * 3 == degrees) {
            return this.rotation + 180;
        } else {
            return this.rotation;
        }
    }
}
