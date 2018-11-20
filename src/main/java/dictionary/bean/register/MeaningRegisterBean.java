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
    @Getter
    private String selectedMeanId;
    @Getter
    private Meaning selectedMean;
    public void setSelectedMeanId(String selectedMeanId) {
        this.selectedMeanId  = selectedMeanId;
        selectedMean = meaningFacade.findOne(Integer.parseInt(selectedMeanId));
        selectedMean.setClarifier(clarifier);
    }

    public void init() {
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

    /* meaning名更新 */
    public String update() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String originWord = meaningFacade.findOne(selectedMean.getId()).getWord();
        if(meaningFacade.update(selectedMean)) {
            facesContext.getExternalContext().getFlash().put("notice",  originWord + " → "+ selectedMean.getWord() + "更新成功");
        } else facesContext.getExternalContext().getFlash().put("notice", "更新失敗：すでに存在する名前です");

        return "/meaning/create?faces-redirect=true&id="+clarifierId;
    }

    /* meaning削除 */
    public String delete() {
        /*
          関連するViewSymbolも削除予定
        */
        meaningFacade.remove(meaningFacade.findOne(Integer.parseInt(selectedMeanId)));

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getFlash().put("notice", selectedMean.getWord()+"削除成功");

        return "/meaning/create?faces-redirect=true&id="+clarifierId;
    }
}
