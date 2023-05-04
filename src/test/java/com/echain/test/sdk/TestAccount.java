package com.echain.test.sdk;

import com.echain.account.Account;


public class TestAccount {
    public static void main(String[] args) throws Exception {
        Account account = Account.generateAccount();
        System.out.println("address:" + account.getAddress());
        System.out.println("private:" + account.getPrivateKey());

        long start = System.currentTimeMillis();
        for(int i=0; i<10000; i++){
            Account account1 = Account.generateAccount();
        }
        long end = System.currentTimeMillis();
        System.out.println("Generating 10000 account time used:" + (end-start) + "ms.");
    }
}
