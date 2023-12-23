package model;

public class exchangeRatesModel {
    private int ID;
    private double Rate;
    private currenciesModel BaseCurrencyid;
    private currenciesModel TargetCurrencyid;

    private double amount;
    private double convertedAmount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public currenciesModel getBaseCurrencyid() {
        return BaseCurrencyid;
    }

    public void setBaseCurrencyid(currenciesModel baseCurrencyid) {
        BaseCurrencyid = baseCurrencyid;
    }

    public currenciesModel getTargetCurrencyid() {
        return TargetCurrencyid;
    }

    public void setTargetCurrencyid(currenciesModel targetCurrencyid) {
        TargetCurrencyid = targetCurrencyid;
    }
}
