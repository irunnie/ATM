package com.kohanevich.controller;

import com.kohanevich.service.Zip;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/zip")
public class ZipController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Zip zip = new Zip();
        zip.generateFileList(new File(Zip.sourceFolder));
        zip.compress(Zip.outputZipFile);

        String filePath = Zip.outputZipFile;
        File downloadFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        ServletContext context = getServletContext();

        String mimeType = context.getMimeType(filePath);

        resp.setContentType(mimeType);
        resp.setContentLength((int)downloadFile.length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("filename=\"log-archive.zip\"", downloadFile.getName());
        resp.setHeader(headerKey, headerValue);

        OutputStream outputStream = resp.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }
}
