package ru.t1.apupynin.creditms.service;

import org.springframework.stereotype.Service;

@Service
public class CreditDecisionService {

    public void processCreditRequest(Object payload, long creditLimit) {
        // TODO: 1) Call MS-1 to get client full name and doc number
        // TODO: 2) Evaluate existing credit products against limit and delinquencies
        // TODO: 3) Approve/decline
        // TODO: 4) On approval, open product and generate schedule using annuity formula
    }
}


