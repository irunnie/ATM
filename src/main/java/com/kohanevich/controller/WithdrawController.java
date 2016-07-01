package com.kohanevich.controller;

import com.kohanevich.service.AtmCalculator;
import com.kohanevich.service.Status;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@WebServlet("/withdraw")
public class WithdrawController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/withdraw.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AtmCalculator atmCalculator = AtmCalculator.getInstance();

        int withdrawAmount = Integer.parseInt(req.getParameter("withdrawAmount"));
        Status currentStatus = atmCalculator.withdraw(withdrawAmount);

        if(currentStatus == Status.AVAILABLE){
            AtmCalculator.log.info("=============================================");
            AtmCalculator.log.info("withdraw amount = " + withdrawAmount + ";");
            for (Map.Entry<Integer, Integer> entry : atmCalculator.map4logger.entrySet()) {
                AtmCalculator.log.info("denomination = " + entry.getKey() + "; "
                        + "banknotes amount = " + entry.getValue() + ";");
            }
            AtmCalculator.log.info("=============================================");
            req.getRequestDispatcher("/pages/success_withdraw_page.jsp").forward(req, resp);
        }
        else if (currentStatus == Status.AVAILABLE_ONLY){
            int availableAmount = currentStatus.amount;
            req.setAttribute("availableAmount", availableAmount);
            req.getRequestDispatcher("/pages/available.jsp").forward(req, resp);
        }
        else if (atmCalculator.withdraw(withdrawAmount) == Status.EMPTY_ATM){
            req.getRequestDispatcher("/pages/empty_atm.jsp").forward(req, resp);
        }

    }

}


