package dictionary.bean.search;

import dictionary.SubFunction;
import dictionary.entity.MultiviewSymbol;
import dictionary.facade.MultiviewSymbolFacade;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class MultiviewSymbolSearchBean extends SubFunction implements Serializable {
    @Inject
    MultiviewSymbolFacade multiviewSymbolFacade;

    @Setter @Getter
    private List<MultiviewSymbol> multiviewSymbolList = new ArrayList<>();

    public void init() {
        multiviewSymbolList = multiviewSymbolFacade.findAll();
    }

}
