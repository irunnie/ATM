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

        String amount = req.getParameter("withdrawAmount");
        int withdrawAmount = amount.equals("") ? 0 : Integer.parseInt(amount);
        Status currentStatus = atmCalculator.withdraw(withdrawAmount);

        if(currentStatus == Status.AVAILABLE){
            req.getRequestDispatcher("/pages/success_page.jsp").forward(req, resp);
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


