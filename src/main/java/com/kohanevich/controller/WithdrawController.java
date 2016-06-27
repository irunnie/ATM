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

/**
 * Created by Closed on 6/25/2016
 */
@WebServlet("/withdraw")
public class WithdrawController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/withdraw.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int withdrawAmount;
        AtmCalculator atmCalculator = AtmCalculator.INSTANCE;

        if(req.getParameter("withdrawAmount") == null){
            int availableAmount = Integer.parseInt(req.getParameter("hiddenValue"));
            atmCalculator.withdraw(availableAmount);
            req.getRequestDispatcher("/pages/successpage.jsp").forward(req, resp);
        }
        else {
            withdrawAmount = Integer.parseInt(req.getParameter("withdrawAmount"));

            if (atmCalculator.withdraw(withdrawAmount) == Status.AVAILABLE) {
                req.getRequestDispatcher("/pages/successpage.jsp").forward(req, resp);
            } else if (atmCalculator.withdraw(withdrawAmount) == Status.AVAILABLE_ONLY) {
                int availableAmount = atmCalculator.withdraw(withdrawAmount).amount;
                req.setAttribute("availableAmount", availableAmount);
                req.getRequestDispatcher("/pages/available.jsp").forward(req, resp);
            } else if (atmCalculator.withdraw(withdrawAmount) == Status.EMPTY_ATM) {
                req.getRequestDispatcher("/pages/emptyatm.jsp").forward(req, resp);
            }
        }
    }
}
