package dictionary;

import dictionary.entity.ViewSymbol;
import dictionary.entity.ViewSymbolHasMeaning;
import dictionary.facade.ViewSymbolFacade;
import dictionary.facade.ViewSymbolHasMeaningFacade;

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
    ViewSymbolFacade viewSymbolFacade;
    @Inject
    ViewSymbolHasMeaningFacade viewSymbolHasMeaningFacade;

    /*
    * ファイル操作系
    * */

    public void createDir(String path) {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources") + path;

        File newfile = new File(filepath);

        if (newfile.mkdir()) System.out.println("ディレクトリの作成に成功しました");
        else System.out.println("ディレクトリの作成に失敗しました");
    }

    public static void deleteDir(String dirPath) throws Exception {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources") + dirPath;

        File file = new File(filepath);
        recursiveDeleteFile(file);
    }

    private static void recursiveDeleteFile(final File file) throws Exception {
        // 存在しない場合は処理終了
        if (!file.exists()) {
            return;
        }
        // 対象がディレクトリの場合は再帰処理
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                recursiveDeleteFile(child);
            }
        }
        // 対象がファイルもしくは配下が空のディレクトリの場合は削除する
        file.delete();
    }

    public Boolean uploadImage(Part file, String path, String fileName) {
        try {
            InputStream in = file.getInputStream();
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String filepath = ctx.getRealPath("resources") + path;
            String filename = fileName + ".jpg";

            try {
                BufferedImage image = ImageIO.read(in);

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


    /*
    * DB削除系
    * */

    public void deleteViewSymbol(ViewSymbol viewSymbol) {
        for(ViewSymbolHasMeaning vshm : viewSymbol.getViewSymbolHasMeanings())
            viewSymbolHasMeaningFacade.remove(vshm);

        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String caption = viewSymbol.getMultiviewSymbol().getCaption();
        String filepath = "/vs/"+ caption;

        // View Symbol画像削除
        deleteFile(filepath+"/s/", caption+"-"+viewSymbol.getImage().getId()+"_s.jpg");
        deleteFile(filepath+"/m/", caption+"-"+viewSymbol.getImage().getId()+"_m.jpg");
        deleteFile(filepath+"/l/", caption+"-"+viewSymbol.getImage().getId()+"_l.jpg");

        // 画像削除
        if(viewSymbol.getImage().getViewSymbols().size() == 1)
            deleteFile("/image/", viewSymbol.getImage().getId() + ".jpg");

        viewSymbol.setMultiviewSymbol(null);
        viewSymbol.setViewSymbolHasMeanings(null);
        viewSymbolFacade.remove(viewSymbol);
    }
}
