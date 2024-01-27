package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.constantrunnables.spellcasting;

public class Fireball extends spellcasting {

    private int progress = 0;

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public void incrementProgress() {this.progress = getProgress() + 1;}
    public void cast() {

        if (getProgress() < 10)
        {
            System.out.println("Fireball!");
        }
        else
        {
            this.cancel();
        }

        incrementProgress();
    }

    @Override
    public void run() {
        cast();
    }

}
