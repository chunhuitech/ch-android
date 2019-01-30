package com.chunhuitech.reader.entity;

public class PlaySoundParam {
    private String mp3Path;
    private int beginPoint;
    private int endPoint;

    public String getMp3Path() {
        return mp3Path;
    }

    public long getDelay() {
        long result = endPoint - beginPoint;
        if (result < 0) {
            result = 1;
        }
        return result;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    public int getBeginPoint() {
        return beginPoint;
    }

    public void setBeginPoint(int beginPoint) {
        this.beginPoint = beginPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }
}
