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
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ClarifierRegisterBean extends SubFunction implements Serializable {
    @Inject
    ClarifierFacade clarifierFacade;
    @Inject
    MeaningFacade meaningFacade;

    @Setter @Getter
    private Clarifier clarifier = new Clarifier();
    @Setter @Getter
    private Part file;
    @Setter @Getter
    private List<Clarifier> clarifierList = new ArrayList<>();

    @Getter
    private String selectedClariId;
    @Getter
    private Clarifier selectedClari;
    public void setSelectedClariId(String selectedClariId) {
        this.selectedClariId  = selectedClariId;
        selectedClari = clarifierFacade.findOne(Integer.parseInt(selectedClariId));
    }

    public void init() {
        clarifierList = clarifierFacade.findAll();
    }

    /* 新規clarifier登録 */
    public String register() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // アイコンアップロード
        if(!uploadIcon(file, "/c_icon/", clarifier.getTypeName())) {
            facesContext.getExternalContext().getFlash().put("notice", "登録失敗：アイコンが正方形ではありません");
            return "/clarifier/create?faces-redirect=true";
        }

        if(clarifierFacade.create(clarifier)) {
            facesContext.getExternalContext().getFlash().put("notice", clarifier.getTypeName()+"登録成功");
        } else {
            deleteFile("/c_icon/", clarifier.getTypeName()+".jpg");
            facesContext.getExternalContext().getFlash().put("notice", "登録失敗：すでに存在する名前です");
        }

        return "/clarifier/create?faces-redirect=true";
    }

    /* clarifier名更新 */
    public String update() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String originWord = clarifierFacade.findOne(selectedClari.getId()).getTypeName();
        System.out.println("update");
        System.out.println(originWord);
        if(clarifierFacade.update(selectedClari)) {
            facesContext.getExternalContext().getFlash().put("notice",  originWord + " → "+ selectedClari.getTypeName() + "更新成功");
            updateFileName("/c_icon/", originWord+".jpg", selectedClari.getTypeName()+".jpg");
        } else facesContext.getExternalContext().getFlash().put("notice", "更新失敗：すでに存在する名前です");

        return "/clarifier/create?faces-redirect=true";
    }

    /* clarifier削除 */
    public String delete() {
        /*
          関連するViewSymbolも削除予定
        */
        Clarifier clarifier = clarifierFacade.findOne(Integer.parseInt(selectedClariId));
        if(clarifier.getMeanings() != null) {
            for(Meaning meaning : clarifier.getMeanings()) {
                meaningFacade.remove(meaning);
            }
        }
        clarifier.setMeanings(null);
        clarifierFacade.remove(clarifier);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getFlash().put("notice", selectedClari.getTypeName()+"削除成功");

        return "/clarifier/create?faces-redirect=true";
    }

    public String linkMeaning(int id) {
        System.out.println("link");
        return "/meaning/create?faces-redirect=true&id="+id;
    }
}
