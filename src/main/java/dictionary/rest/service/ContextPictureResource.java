package dictionary.rest.service;


import dictionary.entity.Clarifier;
import dictionary.entity.MultiviewSymbol;
import dictionary.facade.*;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Path("/symbol")
public class ContextPictureResource implements Serializable {
    @EJB
    MultiviewSymbolFacade multiviewSymbolFacade;
    @EJB
    ClarifierFacade clarifierFacade;

    public ContextPictureResource() {
        System.out.println("test");
    }

    /**
     * GET http://[ホスト名]/Context/api/symbol/images[/[形容詞名]][?query=[パラメーター]]
     *
     * ex)
     * URI: http://localhost:8080/Dictionary/api/multiview_symbol/beautiful?query=test
     */
    @GET
    @Path("/multiview_symbol/{caption}/")
    @Produces({"application/json"})
    public RestMultiviewSymbol retrieveMultiple(@Context UriInfo uriInfo , @Context FacesContext fcx) {
        String caption = uriInfo.getPathParameters().getFirst("caption");
        String size = uriInfo.getQueryParameters().getFirst("size");

        System.out.println("size : " + uriInfo.getQueryParameters().size());
        System.out.println("caption : " + caption);

        //ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        //String filepath = ctx.getRealPath("resources") +  "/images/";

        List<Clarifier> clarifierTypes = new ArrayList<>();
        System.out.println(clarifierFacade.findOne(1));
        clarifierTypes.add(clarifierFacade.findOne(1));
        System.out.println(clarifierTypes.size());

        RestMultiviewSymbol resultMultiviewSymbol
                = new RestMultiviewSymbol(multiviewSymbolFacade.findOneByCap(caption), "test", clarifierTypes);

        //resultMultiviewSymbol.addClarifierToClarifierTypes(clarifierFacade.findOne(1));

        return resultMultiviewSymbol;
    }
}
