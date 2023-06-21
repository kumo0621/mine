package com.github.kumo0621.mine;

import java.util.Random;

public class RandomCount {
    public static int random() {
        Random rand = new Random();
        return rand.nextInt(100);
    }
}

