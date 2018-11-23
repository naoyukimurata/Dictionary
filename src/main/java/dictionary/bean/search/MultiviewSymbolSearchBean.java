package dictionary.bean.search;

import dictionary.SubFunction;
import dictionary.entity.MultiviewSymbol;
import dictionary.entity.ViewSymbol;
import dictionary.facade.MultiviewSymbolFacade;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    public Object[] getPictures(MultiviewSymbol multiviewSymbol) {
        List<ViewSymbol> viewSymbolList = new ArrayList<>();
        if(multiviewSymbol.getViewSymbols() != null) {
            for(ViewSymbol viewSymbol : multiviewSymbol.getViewSymbols()) {
                viewSymbolList.add(viewSymbol);
            }
        }
        return viewSymbolList.toArray();
    }

    public void delete(ViewSymbol viewSymbol) {
        System.out.println(viewSymbol.getName());
        deleteViewSymbol(viewSymbol);

        FacesMessage fm2 = new FacesMessage("View Symbolを削除しました");
        FacesContext fc2 = FacesContext.getCurrentInstance();
        fc2.addMessage(null, fm2);

        init();
    }
}
