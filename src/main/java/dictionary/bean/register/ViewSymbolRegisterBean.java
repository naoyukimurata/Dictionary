package dictionary.bean.register;

import dictionary.SubFunction;
import dictionary.entity.Image;
import dictionary.entity.Meaning;
import dictionary.entity.MultiviewSymbol;
import dictionary.facade.ClarifierFacade;
import dictionary.facade.ImageFacade;
import dictionary.facade.MeaningFacade;
import dictionary.facade.MultiviewSymbolFacade;
import lombok.Getter;
import lombok.Setter;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ViewSymbolRegisterBean extends SubFunction implements Serializable {
    @Inject
    MeaningFacade meaningFacade;
    @Inject
    ClarifierFacade clarifierFacade;
    @Inject
    ImageFacade imageFacade;
    @Inject
    MultiviewSymbolFacade multiviewSymbolFacade;

    @Setter @Getter
    private int msId;
    @Setter @Getter
    private MultiviewSymbol multiviewSymbol;

    @Getter @Setter
    private String selectedObjectId = "-1";
    @Getter @Setter
    private String selectedObjectName;

    @Getter @Setter
    private String selectedTimeId = "-1";
    @Getter @Setter
    private String selectedTimeName;

    @Getter @Setter
    private String selectedPlaceId = "-1";
    @Getter @Setter
    private String selectedPlaceName;

    @Getter @Setter
    private String selectedSituId = "-1";
    @Getter @Setter
    private String selectedSituName;

    @Getter @Setter
    private String selectedIndId = "-1";
    @Getter @Setter
    private String selectedIndName;

    @Getter @Setter
    private String selectedCollId = "-1";
    @Getter @Setter
    private String selectedCollName;

    @Setter @Getter
    private Part file;
    @Setter @Getter
    private Image image = new Image();

    public void init() {
        image.setName("TEST");
        multiviewSymbol = multiviewSymbolFacade.findOne(msId);
    }

    public Map<String, String> getObjects() {
        Map<String, String> objects = new LinkedHashMap<>();
        objects.put("", "");

        for(Meaning meaning : clarifierFacade.findOneByTypeName("Object").getMeanings()) {
            objects.put(meaning.getWord(), meaning.getId().toString());
        }

        return objects;
    }
    public void object() {
        if(selectedObjectId != null) {
            selectedObjectName = meaningFacade.findOne(Integer.parseInt(selectedObjectId)).getWord();
        } else {
            selectedObjectId = "-1";
            selectedObjectName = null;
        }
        //initPicList();
        //if(allNull()) judge();
    }

    public Map<String, String> getTimes() {
        Map<String, String> times = new LinkedHashMap<>();
        times.put("", "");

        for(Meaning meaning : clarifierFacade.findOneByTypeName("Time").getMeanings()) {
            times.put(meaning.getWord(), meaning.getId().toString());
        }

        return times;
    }
    public void time() {
        if(selectedTimeId != null) {
            selectedTimeName = meaningFacade.findOne(Integer.parseInt(selectedTimeId)).getWord();
        } else {
            selectedTimeId = "-1";
            selectedTimeName = null;
        }
        //initPicList();
        //if(allNull()) judge();
    }

    public Map<String, String> getPlaces() {
        Map<String, String> places = new LinkedHashMap<>();
        places.put("", "");

        for(Meaning meaning : clarifierFacade.findOneByTypeName("Place").getMeanings()) {
            places.put(meaning.getWord(), meaning.getId().toString());
        }

        return places;
    }
    public void place() {
        if(selectedPlaceId != null) {
            selectedPlaceName = meaningFacade.findOne(Integer.parseInt(selectedPlaceId)).getWord();
        } else {
            selectedPlaceId = "-1";
            selectedPlaceName = null;
        }
        //initPicList();
        //if(allNull()) judge();
    }

    public Map<String, String> getSituations() {
        Map<String, String> situations = new LinkedHashMap<>();
        situations.put("", "");

        for(Meaning meaning : clarifierFacade.findOneByTypeName("Situation").getMeanings()) {
            situations.put(meaning.getWord(), meaning.getId().toString());
        }

        return situations;
    }
    public void situation() {
        if(selectedSituId != null) {
            selectedSituName = meaningFacade.findOne(Integer.parseInt(selectedSituId)).getWord();
        } else {
            selectedSituId = "-1";
            selectedSituName = null;
        }
        //initPicList();
        //if(allNull()) judge();
    }

    public Map<String, String> getIndividuals() {
        Map<String, String> individuals = new LinkedHashMap<>();
        individuals.put("", "");

        for(Meaning meaning : clarifierFacade.findOneByTypeName("Individual").getMeanings()) {
            individuals.put(meaning.getWord(), meaning.getId().toString());
        }

        return individuals;
    }
    public void individual() {
        if(selectedIndId != null) {
            selectedIndName = meaningFacade.findOne(Integer.parseInt(selectedIndId)).getWord();
        } else {
            selectedIndId = "-1";
            selectedIndName = null;
        }
        //initPicList();
        //if(allNull()) judge();
    }

    public Map<String, String> getCollectives() {
        Map<String, String> collectives = new LinkedHashMap<>();
        collectives.put("", "");

        for(Meaning meaning : clarifierFacade.findOneByTypeName("Collective").getMeanings()) {
            collectives.put(meaning.getWord(), meaning.getId().toString());
        }

        return collectives;
    }
    public void collective() {
        if(selectedCollId != null) {
            selectedCollName = meaningFacade.findOne(Integer.parseInt(selectedCollId)).getWord();
        } else {
            selectedCollId = "-1";
            selectedCollName = null;
        }
        //initPicList();
        //if(allNull()) judge();
    }

    public void create() {
        // 画像保存
        imageFacade.create(image);
        image.setName(Integer.toString(image.getId()));
        imageFacade.update(image);
        uploadImage(file,"/image/",Integer.toString(image.getId()));

        // View Symbol作成
        imgProc(getImg("/image/"+Integer.toString(image.getId())+".jpg"), "/vs/"+multiviewSymbol.getCaption()+"/m/");
    }

    public BufferedImage getImg(String imageUrl) {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources");

        File file = new File(filepath + imageUrl);
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private void imgProc(BufferedImage bufImg, String output) {

        int w = bufImg.getWidth();
        int h = bufImg.getHeight();

        int SIZE = 600;
        int width = SIZE,height = SIZE;

        BufferedImage squBufImg = null;
        if(w > h) {
            height = width * h / w;
            squBufImg = new BufferedImage(SIZE, height + 100, BufferedImage.TYPE_INT_BGR);
        } else {
            width = 500  * w / h;
            height = 500;
            squBufImg = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_BGR);
        }

        AffineTransformOp xform = new AffineTransformOp( AffineTransform.getScaleInstance( ( double )width / w, ( double )height / h ), AffineTransformOp.TYPE_BILINEAR );
        BufferedImage dst = new BufferedImage(width, height, bufImg.getType() );
        xform.filter(bufImg, dst);

        int white = 255, black = 0, rgb = 0;
        if(width == SIZE) {
            for(int y = 0 ; y < height+100 ; y++) {
                for (int x = 0; x < width; x++) {
                    if(y == 0 || x == width-1 || y == height) rgb = 0xff000000 | black << 16 | black << 8 | black;
                    else if(y < height) {
                        if(x == 0) rgb = 0xff000000 | black << 16 | black << 8 | black;
                        else rgb = dst.getRGB(x,y);
                    } else {
                        if(x!= 0 && x%100==0) rgb = 0xff000000 | black << 16 | black << 8 | black;
                        else if(x == 0 || x==width-1 || y==height+99) rgb =  0xff000000 | black << 16 | black << 8 | black;
                        else rgb = 0xff000000 | white << 16 | white << 8 | white;
                    }
                    squBufImg.setRGB(x,y,rgb);
                }
            }
        } else {
            int space = (600-width)/2;
            for(int y = 0 ; y < SIZE ; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if(y == 0) {
                        rgb =  0xff000000 | black << 16 | black << 8 | black;
                    } else if(y < 500) {
                        if(x < space) {
                            if(x == 0 || y == 0) rgb = 0xff000000 | black << 16 | black << 8 | black;
                            else rgb = 0xff000000 | white << 16 | white << 8 | white;
                        } else if(x < space+width) {
                            rgb = dst.getRGB(x-space,y);
                        } else {
                            if(x == 0 || y == 0 || y==SIZE-1 || x==SIZE-1) rgb =  0xff000000 | black << 16 | black << 8 | black;
                            else rgb = 0xff000000 | white << 16 | white << 8 | white;
                        }
                    } else {
                        if((x!= 0 && x%100==0) || y == 500) rgb =  0xff000000 | black << 16 | black << 8 | black;
                        else if(x == 0 || x==SIZE-1 || y==SIZE-1) rgb =  0xff000000 | black << 16 | black << 8 | black;
                        else rgb = 0xff000000 | white << 16 | white << 8 | white;
                    }
                    squBufImg.setRGB(x,y,rgb);
                }
            }
        }

        // アイコン追加
        /*if(selectedWhenId != "-1") {
            TagIcon tagIcon = tagIconFacade.findOneById(Integer.parseInt(selectedWhenId));
            cPicTitle += "_" + tagIcon.getName();
            tagIconList.add(tagIcon);
            squBufImg = addIcon(10, squBufImg, getImg("/tag_icon/when/" + tagIcon.getName() + ".jpg"));
        }
        if(selectedWhereId != "-1") {
            TagIcon tagIcon = tagIconFacade.findOneById(Integer.parseInt(selectedWhereId));
            cPicTitle += "_" + tagIcon.getName();
            tagIconList.add(tagIcon);
            squBufImg = addIcon(110, squBufImg, getImg("/tag_icon/where/" + tagIcon.getName() + ".jpg"));
        }
        if(selectedWhoId != "-1") {
            TagIcon tagIcon = tagIconFacade.findOneById(Integer.parseInt(selectedWhoId));
            cPicTitle += "_" + tagIcon.getName();
            tagIconList.add(tagIcon);
            squBufImg = addIcon(210, squBufImg, getImg("/tag_icon/who/" + tagIcon.getName() + ".jpg"));
        }
        if(selectedWhatId != "-1") {
            TagIcon tagIcon = tagIconFacade.findOneById(Integer.parseInt(selectedWhatId));
            cPicTitle += "_" + tagIcon.getName();
            tagIconList.add(tagIcon);
            squBufImg = addIcon(310, squBufImg, getImg("/tag_icon/what/" + tagIcon.getName() + ".jpg"));
        }
        if(selectedWhyId != "-1") {
            TagIcon tagIcon = tagIconFacade.findOneById(Integer.parseInt(selectedWhyId));
            cPicTitle += "_" + tagIcon.getName();
            tagIconList.add(tagIcon);
            squBufImg = addIcon(410, squBufImg, getImg("/tag_icon/why/" + tagIcon.getName() + ".jpg"));
        }*/
        //cPicTitle += "_" + tagIcon.getName();
        //tagIconList.add(tagIcon);
        //squBufImg = addIcon(510, squBufImg, getImg("/tag_icon/how/" + tagIcon.getName() + ".jpg"));

        // 一度byte配列変換
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream os = new BufferedOutputStream(baos);
        try {
            //ImageIO.write(squBufImg,"jpg",os);
            ImageIO.write(squBufImg,"jpg",os);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageInByte = baos.toByteArray();
        // byte配列をinputstreamに変換
        InputStream is = new ByteArrayInputStream(imageInByte);

        String filename = "test_m" + ".jpg";
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources");
        try {
            Files.copy(is, new File(filepath + output, filename).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return is;
    }
}
