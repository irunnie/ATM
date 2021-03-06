package com.kohanevich.service;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.kohanevich.service.Status.*;


public class AtmCalculator implements Calculator {

    public static Logger log = Logger.getLogger(AtmCalculator.class);
    private static final Integer MAX_COUNT_BANKNOTES = getCount();
    private static final Integer MAX_CAPACITY_BANKNOTES = getCapacity();
    private static Properties banknotes;
    private Map<Integer, Integer> atm = new ConcurrentHashMap<>();
    private static volatile AtmCalculator instance;


    private static Integer getCount() {
        banknotes = new Properties();
        try {
            banknotes.load(AtmCalculator.class.getResourceAsStream("/capacity-count.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(banknotes.getProperty("MAX_COUNT_BANKNOTES"));
    }

    private static Integer getCapacity() {
        banknotes = new Properties();
        try {
            banknotes.load(AtmCalculator.class.getResourceAsStream("/capacity-count.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(banknotes.getProperty("MAX_CAPACITY_BANKNOTES"));
    }

    public static AtmCalculator getInstance() {
        AtmCalculator localInstance = instance;
        if (localInstance == null)
        {
            synchronized (AtmCalculator.class){
                localInstance = instance;
                if (localInstance == null){
                    instance = localInstance = new AtmCalculator();
                }
            }
        }
        return localInstance;

    }
    private AtmCalculator() {
        Properties initialization = new Properties();
        try
        {
            initialization.load(AtmCalculator.class.getResourceAsStream("/denomination-amount.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Object, Object> entry : initialization.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            atm.put(Integer.parseInt(key), Integer.parseInt(value));
        }
    }


    public Status withdraw(int requestedAmount) {
        List<Integer> denominations = new ArrayList<>(atm.keySet());
        Collections.sort(denominations);

        if (requestedAmount <= 0) {
            return offerMinDenomination();
        }

        int remain = requestedAmount;

        int totalBanknotes = 0;
        HashMap<Integer, Integer> map = Maps.newHashMap();
        Collections.reverse(denominations);

        for (Integer denomination : denominations) {

            if (remain >= denomination && atm.get(denomination) != 0 && totalBanknotes < MAX_COUNT_BANKNOTES) {
                int currentBanknotes = remain / denomination;
                if (atm.get(denomination) < currentBanknotes) {
                    currentBanknotes = atm.get(denomination);
                }
                if (totalBanknotes + currentBanknotes <= MAX_COUNT_BANKNOTES) {
                    totalBanknotes += currentBanknotes;
                    remain -= currentBanknotes * denomination;
                    map.put(denomination, currentBanknotes);
                } else {
                    int availableCount = MAX_COUNT_BANKNOTES - totalBanknotes;
                    totalBanknotes += availableCount;
                    remain -= denomination * availableCount;
                    map.put(denomination, availableCount);
                }
            }
        }

        if (map.isEmpty()) {
            return offerMinDenomination();
        } else if (remain != 0) {
            return build(AVAILABLE_ONLY, requestedAmount - remain);
        } else {
            log.info("withdraw amount = " + requestedAmount + ";");
            map.forEach((key, value) -> log.info("denomination = " + key + "; "
                                         + "banknotes amount = " + value + ";"));
            log.info("=============================================");
            map.forEach((key,value) -> atm.put(key, atm.get(key) - value));
            return Status.AVAILABLE;
        }
    }

    private Status offerMinDenomination() {
        return atm.keySet().stream().sorted()
                .filter(input -> atm.get(input) > 0).findFirst()
                .map(input -> build(AVAILABLE_ONLY, input))
                .orElse(EMPTY_ATM);
    }

    @Override
    public Status deposit(int denomination) {
        if (checkBanknotesOverflow(denomination)){
            log.info("deposit amount = " + denomination + ";");
            log.info("=============================================");
            atm.put(denomination, atm.get(denomination) + 1);
            return Status.AVAILABLE;
        }
        else {
            return Status.BANKNOTES_OVERFLOW;
        }
    }

    private boolean checkBanknotesOverflow(int denomination){
        return atm.get(denomination) < MAX_CAPACITY_BANKNOTES;
    }

    public Map<Integer, Integer> getAtm() {
        return atm;
    }

    public void setAtm(Map<Integer, Integer> atm) {
        this.atm = atm;
    }

}

