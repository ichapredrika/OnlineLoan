package com.arie.onlineloan.models;

import org.json.JSONObject;

public class Transaction {
    private String id;
    private String status;
    private String type;
    private String loanAmount;
    private String installment;

    public Transaction(){

    }

    public Transaction(JSONObject object) {
        try {
            this.id = object.getString("ID");
            this.status = object.getString("STATUS");
            this.type = object.getString("LOAN_TYPE");
            this.loanAmount = object.getString("TOTAL_LOAN");
            this.installment = object.getString("INSTALLMENT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Transaction(String id, String status, String type, String loanAmount, String installment) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.loanAmount = loanAmount;
        this.installment = installment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }
}
