package com.kohanevich.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Denis on 6/25/2016
 */
public class AtmCalculator implements Calculator {

    private static final Integer MAX_COUNT_BANKNOTES = 10;
    private static final Integer MAX_CAPACITY_BANKNOTES = 20;
    private Map<Integer, Integer> atm = new HashMap<>();

    FileInputStream fileInputStream;
    Properties properties = new Properties();

    public AtmCalculator() throws IOException {
//        atm.put(20, 20);
//        atm.put(50, 20);
//        atm.put(100, 20);
//        atm.put(200, 20);
//        atm.put(500, 20);
        try {
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new IOException("Cannot initialize constructor with config.property file");
        }
        
    }

    public AtmCalculator(Map<Integer, Integer> atm) {
        this.atm = atm;
    }

    public Status withdraw(Integer amount) {
        List<Integer> denominations = new ArrayList<>(atm.keySet());
        Collections.sort(denominations);

        if (amount <= 0) {
            Collections.sort(denominations);
            for (Integer denomination : denominations) {
                if (atm.get(denomination) > 0) {
                    Status error = Status.ERROR;
                    error.amount = denomination;
                    return error;
                }
            }
            return Status.ERROR;
        }

            int calculAmount = amount;
       /* atm.keySet().stream().sorted((i1, i2) -> i2.compareTo(i1)).forEach(i -> {
            for () {
                if (calculAmount > i) {
                    int countToWithdraw = calculAmount / i;
                    int remain = calculAmount % i;
                    atm.put(i, atm.get(i) - countToWithdraw);
                }
            }
        });*/
        int totalBanknotes = 0;
        boolean notifyUser = false;
        HashMap<Integer, Integer> map = new HashMap<>();


        Collections.reverse(denominations);

        for (Integer denomination : denominations) {
            if (calculAmount >= denomination && atm.get(denomination) != 0 && totalBanknotes < MAX_COUNT_BANKNOTES) {
                int currentBanknotes = calculAmount / denomination;
                if (atm.get(denomination) < currentBanknotes) {
                    currentBanknotes = atm.get(denomination);
                }
                if (totalBanknotes + currentBanknotes <= MAX_COUNT_BANKNOTES) {
                    totalBanknotes += currentBanknotes;
                    calculAmount = calculAmount % denomination;
                    map.put(denomination, currentBanknotes);
                } else {
                    int availableCount = MAX_COUNT_BANKNOTES - totalBanknotes;
                    totalBanknotes += availableCount;
                    calculAmount -= denomination * availableCount;
                    map.put(denomination, availableCount);
                    notifyUser = true;
                }
            }
        }

       if (map.isEmpty()) {
            Collections.sort(denominations);
            for (Integer denomination : denominations) {
                if (atm.get(denomination) > 0) {
                    Status error = Status.ERROR;
                    error.amount = denomination;
                    return error;
                }
            }
            return Status.ERROR;
        } else if (calculAmount != 0 || notifyUser) {
            int availableToWithdraw = amount - calculAmount;
            Status error = Status.ERROR;
            error.amount = availableToWithdraw;
            return error;
        } else {
            for (Integer denomination : map.keySet()) {
                atm.put(denomination, atm.get(denomination) - map.get(denomination));
            }
            return Status.SUCCESS;
        }
    }

    @Override
    public Status deposit(int amount) { //1000
        List<Integer> denominations = new ArrayList<>(atm.keySet());
        Collections.sort(denominations);
        Collections.reverse(denominations);

        for (Integer denomination : denominations) {
            if(amount == 0){
                return Status.SUCCESS;
            }
            int possibleAmount = overflowCheck(denomination);
            int neededAmount = amount / denomination;
            if (possibleAmount >= neededAmount){
                amount = amount % denomination;
                atm.put(denomination, atm.get(denomination) + neededAmount);
            }

        }
        return Status.SUCCESS;
    }


    private int overflowCheck(int denomination) {
        return MAX_CAPACITY_BANKNOTES - atm.get(denomination);
    }

    public Map<Integer, Integer> getAtm() {
        return atm;
    }
}

enum Status {
    SUCCESS, ERROR;
    public int amount;
}
