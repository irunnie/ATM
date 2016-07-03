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

@WebServlet("/deposit")
public class DepositController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/pages/deposit.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AtmCalculator atmCalculator = AtmCalculator.getInstance();

        int depositAmount = Integer.parseInt(req.getParameter("amount"));

        Status depositStatus = atmCalculator.deposit(depositAmount);

        if (depositStatus == Status.BANKNOTES_OVERFLOW) {
            req.getRequestDispatcher("/pages/overflow.jsp").forward(req, resp);
        }

        else if (depositStatus == Status.AVAILABLE){
            req.getRequestDispatcher("/pages/success_deposit_page.jsp").forward(req, resp);
        }

    }
}
