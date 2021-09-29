package com.example;

public class BankNetworkManagement extends LogonManager
{
    public BankNetworkManagement() {
        super("my-mux", "my-send", 300000);
    }
}
