package dictionary.rest.service;

import dictionary.entity.MultiviewSymbol;
import dictionary.entity.ViewSymbol;
import dictionary.entity.ViewSymbolHasMeaning;
import dictionary.facade.*;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
@Path("/dictionary")
public class DictionaryResource implements Serializable {
    @EJB
    MultiviewSymbolFacade multiviewSymbolFacade;

    public DictionaryResource() {
    }

    /**
     * GET http://[ホスト名]/Dictionary/api/dictionary/multiview_symbol/{caption}[?query=[パラメーター]]
     *
     * ex)
     * URI: http://localhost:8080/Dictionary/api/dictionary/multiview_symbol/beautiful/?query=test
     */
    @GET
    @Path("/multiview_symbol/{caption}/")
    @Produces({"application/json"})
    public RestMultiviewSymbol retrieveMultiple(@Context UriInfo uriInfo , @Context ServletContext servletContext, @Context HttpServletRequest request) {
        String caption = uriInfo.getPathParameters().getFirst("caption");
        String object = uriInfo.getQueryParameters().getFirst("object");
        String time = uriInfo.getQueryParameters().getFirst("time");
        String place = uriInfo.getQueryParameters().getFirst("place");
        String situation = uriInfo.getQueryParameters().getFirst("situation");
        String individual = uriInfo.getQueryParameters().getFirst("individual");
        String collective = uriInfo.getQueryParameters().getFirst("collective");
        String imageSize = uriInfo.getQueryParameters().getFirst("size");

        // url
        String path = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/resources";

        // Multiview Symbol セット
        MultiviewSymbol multiviewSymbol = new MultiviewSymbol();
        if(!caption.equals("undefined")) {
            multiviewSymbol = multiviewSymbolFacade.findOneByCap(caption);
        }


        // ClarifierTypesセット
        Map<String, String> symbolGraphic = new HashMap<>();
        symbolGraphic.put("imageUrl", path+"/sg/"+caption+".jpg");
        symbolGraphic.put("name", caption);



        // ClarifierTypesセット
        Map<String, String> clarifierTypes
                = checkClarifierTypes(object, time, place, situation, individual, collective);

        // ViewSymbolリストセット
        List<RestViewSymbol> viewSymbols;
        viewSymbols = setViewSymbols(clarifierTypes, multiviewSymbol, imageSize, path);


        RestMultiviewSymbol resultMultiviewSymbol
                = new RestMultiviewSymbol(multiviewSymbol, imageSize, symbolGraphic, clarifierTypes, viewSymbols);

        return resultMultiviewSymbol;
    }

    public  Map<String, String> checkClarifierTypes(String object, String time, String place,
                                                   String situation, String individual, String collective) {
        Map<String, String> clarifierTypes = new HashMap<>();
        if(object != null) clarifierTypes.put("Object", object);
        if(time != null) clarifierTypes.put("Time", time);
        if(place != null) clarifierTypes.put("Place", place);
        if(situation != null) clarifierTypes.put("Situation", situation);
        if(individual != null) clarifierTypes.put("Individual", individual);
        if(collective != null) clarifierTypes.put("Collective", collective);

        return clarifierTypes;
    }

    public List<RestViewSymbol> setViewSymbols(Map<String, String> clarifierTypes, MultiviewSymbol multiviewSymbol, String imageSize, String path) {
        List<RestViewSymbol> restViewSymbolList = new ArrayList<>();
        int i;
        for(ViewSymbol viewSymbol : multiviewSymbol.getViewSymbols()) {
            if(0 == clarifierTypes.size()) {
                restViewSymbolList.add(new RestViewSymbol(viewSymbol, imageSize ,path));
            } else if(viewSymbol.getViewSymbolHasMeanings().size() >= clarifierTypes.size()) {
                i = 0;
                for (Map.Entry<String, String> entry : clarifierTypes.entrySet()) {
                    for (ViewSymbolHasMeaning vshm : viewSymbol.getViewSymbolHasMeanings()) {
                        if(entry.getKey().equals(vshm.getMeaning().getClarifier().getTypeName())) {
                            if(entry.getValue().equals(vshm.getMeaning().getWord()) || entry.getValue().equals("all")) {
                                //System.out.println("Key:" + entry.getKey() + " Value:" + entry.getValue());
                                //System.out.println("Clarifier:" + vshm.getMeaning().getClarifier().getTypeName() + " Meaning:" + vshm.getMeaning().getWord());
                                i++;
                                break;
                            }
                        }
                    }
                }

                // 追加
                if(i == clarifierTypes.size())
                    restViewSymbolList.add(new RestViewSymbol(viewSymbol, imageSize ,path));
            }
        }

        return restViewSymbolList;
    }
}
