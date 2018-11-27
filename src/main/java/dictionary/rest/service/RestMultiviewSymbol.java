package dictionary.rest.service;

import dictionary.entity.MultiviewSymbol;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class RestMultiviewSymbol {
    @Getter @Setter
    private String caption;
    @Getter @Setter
    private Map<String, String>  symbolGraphic = new HashMap<>();
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

    public RestMultiviewSymbol(MultiviewSymbol multiviewSymbol, String imageSize, Map<String, String> symbolGraphic, Map<String, String> clarifierTypes, List<RestViewSymbol> viewSymbols) {
        this.caption = multiviewSymbol.getCaption();
        this.symbolGraphic = symbolGraphic;
        this.clarifierTypes = clarifierTypes;
        this.viewSymbols = viewSymbols;
        this.size = imageSize==null ? "middle" : imageSize;
        this.num = viewSymbols.size();
        this.total_viewSymbols = multiviewSymbol.getViewSymbols().size();
    }
}
