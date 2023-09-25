package com.mcfall.chameleon;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Game {
    private String code;
    private List<String> players;

    private static Set<String> codes = new HashSet<String>();
    
    private static final int CODE_LENGTH = 4;
    private String chameleon;
    private String current = "";
    private int numberOfPlayers;
    int round;
    Random randomNumberGenerator = new Random();

    public Game(int numberOfPlayers) {
        round = 0;
        this.numberOfPlayers = numberOfPlayers;
        this.players = new LinkedList<String>();
        boolean newCodeGenerated = false;
        String newCode = "";
        chameleon = "";

        while (!newCodeGenerated) {
            newCode = "";
            for (int i = 0; i < CODE_LENGTH; i++) {
                newCode += randomNumberGenerator.nextInt(10);
            }
            if (!codes.contains(newCode)) {
                codes.add(newCode);
                this.code = newCode;
                newCodeGenerated = true;
            }
        }
    }

    public void addPlayers(String name) {
        this.players.add(name);
    }

    public void removePlayer(String name) {
        this.players.remove(name);
    }

    public String getCode() {
        return code;
    }

    public int getNumberofPlayers() {
        return numberOfPlayers;
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public String getNext() {
        round++;
        int number = randomNumberGenerator.nextInt(4)+1;
        int index = randomNumberGenerator.nextInt(4);
        chameleon = players.get(randomNumberGenerator.nextInt(players.size()));
        current = "ABCD".substring(index, index+1) + number;
        return current;
    }

    public String getCurrent() {
        return current;
    }

    public String getChameleon() {
        return chameleon;
    }
}
