package dictionary.bean.register;

import dictionary.SubFunction;
import dictionary.entity.Clarifier;
import dictionary.entity.Meaning;
import dictionary.facade.ClarifierFacade;
import dictionary.facade.MeaningFacade;
import lombok.Getter;
import lombok.Setter;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class MeaningRegisterBean extends SubFunction implements Serializable {
    @Inject
    MeaningFacade meaningFacade;
    @Inject
    ClarifierFacade clarifierFacade;

    @Setter @Getter
    private int clarifierId;
    @Setter @Getter
    private Clarifier clarifier;

    @Setter @Getter
    private Meaning meaning = new Meaning();
    @Getter
    private List<Meaning> meaningList = new ArrayList<>();

    public void init() {
        System.out.println("init()");
        System.out.println(clarifierId);
        clarifier = clarifierFacade.findOne(clarifierId);
        meaning.setClarifier(clarifier);
        for(Meaning meaning : clarifier.getMeanings()) {
            meaningList.add(meaning);
        }
    }

    /* 新規meaning登録 */
    public String register() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(meaningFacade.create(meaning)) {
            facesContext.getExternalContext().getFlash().put("notice", meaning.getWord()+"登録成功");
        } else facesContext.getExternalContext().getFlash().put("notice", "登録失敗：すでに存在する名前です");


        return "/meaning/create?faces-redirect=true&id="+clarifierId;
    }

    /* meaning削除 */
    public String delete(Meaning meaning) {
        return "";
    }
}
