package dictionary.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator(value="fileUploadValidator")
public class FileUploadValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        Part file = (Part) o;
        FacesMessage message = null;
        try {
            if (file == null || file.getSize() <= 0 || file.getContentType().isEmpty())
                message = new FacesMessage("ファイルを選択してください");
            else if(!file.getContentType().endsWith("jpeg") && !file.getContentType().endsWith("jpg") && !file.getContentType().endsWith("png"))
                message = new FacesMessage("jpgまたはpng画像を選択してください");

            if (message != null && !message.getDetail().isEmpty()) {
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage(ex.getMessage()));
        }
    }
}