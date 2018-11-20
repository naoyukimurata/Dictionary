package dictionary.bean.register;

import dictionary.SubFunction;
import dictionary.entity.Clarifier;
import dictionary.facade.ClarifierFacade;
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

    @Setter @Getter
    private Clarifier clarifier = new Clarifier();
    @Setter @Getter
    private Part file;
    @Setter @Getter
    private List<Clarifier> clarifierList = new ArrayList<>();

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

    public String linkMeaning(int id) {
        System.out.println("link");
        return "/meaning/create?faces-redirect=true&id="+id;
    }
}
