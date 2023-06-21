package com.github.kumo0621.minegaty;

import java.util.Random;

public class RandomCount {
    public static int random() {
        Random rand = new Random();
        return rand.nextInt(3);
    }
}

