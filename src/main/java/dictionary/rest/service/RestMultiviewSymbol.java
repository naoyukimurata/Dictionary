package dictionary.rest.service;

import dictionary.entity.Clarifier;
import dictionary.entity.MultiviewSymbol;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class RestMultiviewSymbol {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String caption;
    //@Getter @Setter
    //private String[][] clarifierTypes = new String[][]
    @Getter @Setter
    private String url;

    public RestMultiviewSymbol() {
    }

    public RestMultiviewSymbol(MultiviewSymbol multiviewSymbol, String path, List<Clarifier> clarifierTypes) {
        id = multiviewSymbol.getId();
        caption = multiviewSymbol.getCaption();
        //this.clarifierTypes.add(clarifierTypes.get(0).getTypeName());
        url = path + "/" + caption +  ".jpg";
    }
}
