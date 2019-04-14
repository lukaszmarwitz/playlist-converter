package com.wooky.web.servlets;

import com.wooky.core.Engine;
import com.wooky.web.freemarker.TemplateProvider;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Transactional
@WebServlet("result")
public class Result extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(Result.class);
    private static final String SEARCH = "result";

    @Inject
    private TemplateProvider templateProvider;

    @Inject
    private Engine engine;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Content-Type", "text/html; charset=utf-8");

        final String url = req.getParameter("url");

        LOG.info("Requested URL: {}", url);

        ArrayList<String> urlList = engine.urlList(new URL(url));

        Map<String, Object> model = new HashMap<>();
        model.put("urlList", urlList);

        Template template = templateProvider.getTemplate(getServletContext(), SEARCH);

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
