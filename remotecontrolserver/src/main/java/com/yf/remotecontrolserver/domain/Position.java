package com.yf.remotecontrolserver.domain;

import java.io.Serializable;

public class Position implements Serializable {
    private String cmd;
    private int x;
    private int y;

    public Position(String cmd, int x, int y) {
        super();
        this.cmd = cmd;
        this.x = x;
        this.y = y;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position [cmd=" + cmd + ", x=" + x + ", y=" + y + "]";
    }
}
