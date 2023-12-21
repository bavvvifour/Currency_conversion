package test;

import dao.*;
import  model.currenciesModel;
import model.exchangeRatesModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dao.currenciesDAO.*;

public class main {
    public static void main(String[] args) {
        List<exchangeRatesModel> exchangeRatesModels = exchangeRatesDAO.getAllExchangeRates();
        for (exchangeRatesModel exch: exchangeRatesModels) {
            System.out.println(exch.getID());
            System.out.println(exch.getRate());
            System.out.println(exch.getBaseCurrencyid());
            System.out.println(exch.getTargetCurrencyid());
        }
    }
}