package dictionary.bean.register;

import dictionary.SubFunction;
import dictionary.entity.MultiviewSymbol;
import dictionary.entity.SymbolGraphic;
import dictionary.facade.MultiviewSymbolFacade;
import dictionary.facade.SymbolGraphicFacade;
import lombok.Getter;
import lombok.Setter;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class SymbolGraphicRegisterBean extends SubFunction implements Serializable {
    @Inject
    MultiviewSymbolFacade multiviewSymbolFacade;
    @Inject
    SymbolGraphicFacade symbolGraphicFacade;

    @Setter @Getter
    private Part file;
    @Setter @Getter
    private SymbolGraphic symbolGraphic = new SymbolGraphic();
    @Setter @Getter
    private MultiviewSymbol multiviewSymbol = new MultiviewSymbol();

    @Getter
    private List<SymbolGraphic> symbolGraphicList = new ArrayList<>();

    public void init() {
        symbolGraphicList = symbolGraphicFacade.findAll();
    }

    /* 新規Symbol Graphicとmulti-view Symbol登録 */
    public String register() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Symbol Graphicアップロード
        if(!uploadIcon(file, "/sg/", symbolGraphic.getName())) {
            facesContext.getExternalContext().getFlash().put("notice", "登録失敗：アイコンが正方形ではありません");
            return "/clarifier/create?faces-redirect=true";
        } else {
            createDir("/vs/"+symbolGraphic.getName());
            createDir("/vs/"+symbolGraphic.getName()+"/s");
            createDir("/vs/"+symbolGraphic.getName()+"/m");
            createDir("/vs/"+symbolGraphic.getName()+"/l");
        }

        // Multi-view Symbol作成
        multiviewSymbol.setCaption(symbolGraphic.getName());
        multiviewSymbolFacade.create(multiviewSymbol);
        // Symbol Graphic作成
        symbolGraphic.setMultiviewSymbol(multiviewSymbol);
        symbolGraphicFacade.create(symbolGraphic);

        return "/multiviewSymbol/create?faces-redirect=true";
    }
}
