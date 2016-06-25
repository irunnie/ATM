package com.kohanevich.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Closed on 25.06.2016.
 */

public class AtmCalculator {

    private Map<Integer, Integer> bank = new HashMap<Integer, Integer>();

    public AtmCalculator() {
        bank.put(20, 20);
        bank.put(50, 20);
        bank.put(100, 20);
        bank.put(200, 20);
        bank.put(500, 20);
    }

    public void withdraw(int amount) {
        Integer integer = bank.get(amount);
        if (integer != null) {
            bank.put(amount, bank.get(amount) - 1);
        }
    }

    public int checkAmount() {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : bank.entrySet()) {
            sum += entry.getKey() * entry.getValue();
        }
        return sum;
    }

    public void deposit(int amount){

    }

    public boolean checkDenomination(int denomination){
        if(bank.containsKey(denomination)){
            int value = bank.get(denomination);
            return value < 20;
        }
        return false; // ???
    }
}
