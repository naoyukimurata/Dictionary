package dictionary;

import dictionary.facade.ClarifierFacade;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SubFunction {
    @Inject
    ClarifierFacade clarifierFacade;

    public Boolean uploadIcon(Part file, String path, String fileName) {
        try {
            InputStream in = file.getInputStream();
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String filepath = ctx.getRealPath("resources") + path;
            String filename = fileName + ".jpg";

            try {
                BufferedImage image = ImageIO.read(in);
                // 画像が正方形ではないとき
                if(image.getHeight() != image.getWidth()) return false;

                BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D off = tmp.createGraphics();
                off.drawImage(image, 0, 0, Color.WHITE, null);
                ImageIO.write(tmp, "jpg", new File(filepath+filename));
            } catch (Exception e) {
                System.out.println("error");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void updateFileName(String path, String originName, String newName) {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources") +  path;

        //変更前ファイル
        File originFile = new File(filepath +  originName);
        //変更後ファイル
        File newFile = new File(filepath + newName);

        System.out.println(originFile);
        System.out.println(newFile);

        if(originFile.renameTo(newFile)) System.out.println("ファイル名変更成功 : " + newName);
        else {
            System.out.println("ファイル名変更失敗");
            return;
        }
    }

    public void deleteFile(String path, String fileName) {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources") + path + fileName;
        File file = new File(filepath);
        if(file.delete()) {
            System.out.println("削除成功 : " + filepath);
        } else System.out.println("削除失敗 : " + filepath);
    }
}
