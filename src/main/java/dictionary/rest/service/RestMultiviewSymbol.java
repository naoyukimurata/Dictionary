package dictionary.rest.service;

import dictionary.entity.Clarifier;
import dictionary.entity.MultiviewSymbol;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class RestMultiviewSymbol {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String caption;
    @Getter @Setter
    private Map<String, String> clarifierTypes = new HashMap<>();
    @Getter @Setter
    private int num;
    @Getter @Setter
    private int total_viewSymbols;
    @Getter @Setter
    private String size = "middle";
    @Getter @Setter
    private List<RestViewSymbol> viewSymbols;

    public RestMultiviewSymbol() {
    }

    public RestMultiviewSymbol(MultiviewSymbol multiviewSymbol, String imageSize, Map<String, String> clarifierTypes, List<RestViewSymbol> viewSymbols) {
        this.id = multiviewSymbol.getId();
        this.caption = multiviewSymbol.getCaption();
        this.clarifierTypes = clarifierTypes;
        this.viewSymbols = viewSymbols;
        this.size = imageSize==null ? "middle" : imageSize;
    }
}
