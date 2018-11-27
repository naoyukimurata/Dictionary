package dictionary.rest.service;

import dictionary.entity.Meaning;
import dictionary.entity.MultiviewSymbol;
import dictionary.entity.ViewSymbol;
import dictionary.entity.ViewSymbolHasMeaning;
import lombok.Getter;
import lombok.Setter;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class RestViewSymbol {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String imageUrl;
    @Getter @Setter
    private Map<String, String> clarifierTypes = new HashMap<>();

    public RestViewSymbol() {
    }

    public RestViewSymbol(ViewSymbol viewSymbol, String imageSize, String path) {
        id = viewSymbol.getId();
        name = viewSymbol.getName();
        for(ViewSymbolHasMeaning vshm : viewSymbol.getViewSymbolHasMeanings()) {
            Meaning meaning = vshm.getMeaning();
            this.clarifierTypes.put(meaning.getClarifier().getTypeName(), meaning.getWord());
        }
        checkImageSize(imageSize, viewSymbol, path);
    }

    public void checkImageSize(String imageSize, ViewSymbol viewSymbol, String path) {
        String size;
        if(imageSize == null) size = "m";
        else if(imageSize.equals("middle")) size = "m";
        else if(imageSize.equals("small")) size = "s";
        else size = "l";

        this.imageUrl = path + "/vs/" + viewSymbol.getMultiviewSymbol().getCaption()
                +"/"+size+"/"+viewSymbol.getName()+"_"+size+".jpg";
    }
}
