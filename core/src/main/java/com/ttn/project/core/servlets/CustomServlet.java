package com.ttn.project.core.servlets;

import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class,property = {
        "sling.servlet.paths=" + "/bin/users",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
})
public class CustomServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String path=request.getParameter("path")+"/jcr:content";
        String newTitle =request.getParameter("newTitle");

        ResourceResolver resolver=request.getResourceResolver();

        Resource resource=resolver.resolve(path);

        ModifiableValueMap mvp=resource.adaptTo(ModifiableValueMap.class);
        String prevTitle= null;
        if (mvp != null) {
            prevTitle = mvp.get("jcr:title",String.class);
            mvp.put("jcr:title", newTitle);
        }


        Map<String,String> titles=new HashMap<>();
        titles.put("Prev Title",prevTitle);
        titles.put("New Title",newTitle);

        Gson gson = new Gson();

        resolver.commit();


        response.setStatus(SlingHttpServletResponse.SC_OK);

        response.getWriter().write(gson.toJson(titles));
        resolver.close();

    }
}
