package dictionary.bean.register;

import dictionary.SubFunction;
import dictionary.entity.*;
import dictionary.facade.*;
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
import java.util.*;

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
    @Inject
    ViewSymbolFacade viewSymbolFacade;
    @Inject
    ViewSymbolHasMeaningFacade viewSymbolHasMeaningFacade;

    @Setter @Getter
    private int msId;
    @Setter @Getter
    private MultiviewSymbol multiviewSymbol;

    @Setter @Getter
    private ViewSymbol viewSymbol;

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
    private Image image;

    private List<Meaning> selectedMeanList;

    @Setter @Getter
    private List<ViewSymbol> vsList;

    public void init() {
        image = new Image();
        image.setName("TEST");
        multiviewSymbol = multiviewSymbolFacade.findOne(msId);
        selectedMeanList = new ArrayList<>();
        viewSymbol = new ViewSymbol();

        initPicList();
    }

    public void initPicList() {
        vsList = new ArrayList<>();
        for(ViewSymbol viewSymbol : multiviewSymbol.getViewSymbols()) {
            Boolean j = false;
            vsList.add(viewSymbol);
        }

        if(selectedObjectId != "-1" && selectedObjectId != null)
            checkSameIcon("Object", Integer.parseInt(selectedObjectId));
        if(selectedTimeId != "-1" && selectedTimeId != null)
            checkSameIcon("Time", Integer.parseInt(selectedTimeId));
        if(selectedPlaceId != "-1" && selectedPlaceId != null)
            checkSameIcon("Place", Integer.parseInt(selectedPlaceId));
        if(selectedSituId != "-1" && selectedSituId != null)
            checkSameIcon("Situation", Integer.parseInt(selectedSituId));
        if(selectedIndId != "-1" && selectedIndId != null)
            checkSameIcon("Individual", Integer.parseInt(selectedIndId));
        if(selectedCollId != "-1" && selectedCollId != null)
            checkSameIcon("Collective", Integer.parseInt(selectedCollId));
    }

    public void checkSameIcon(String w, int id) {
        System.out.println("select : " + id);
        Iterator<ViewSymbol> it = vsList.iterator();
        while(it.hasNext()) {
            ViewSymbol vs = it.next();
            int i = 0;
            for(ViewSymbolHasMeaning vshm : vs.getViewSymbolHasMeanings()) {
                if(vshm.getMeaning().getClarifier().getTypeName().equals(w)) {
                    if(vshm.getMeaning().getId() != id) {
                        System.out.println(vs.getName()+":"+vshm.getMeaning().getWord());
                        it.remove();
                        break;
                    }
                    break;
                }
                i++;
            }
            if(i==vs.getViewSymbolHasMeanings().size()) it.remove();
        }
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
        initPicList();
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
        initPicList();
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
        initPicList();
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
        initPicList();
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
        initPicList();
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
        initPicList();
        //if(allNull()) judge();
    }

    // 選択されたMeaningをリストに格納
    public void checkSelect() {
        System.out.println("O:"+selectedObjectId);
        System.out.println("T:"+selectedTimeId);
        System.out.println("P:"+selectedPlaceId);
        System.out.println("S:"+selectedSituId);
        System.out.println("I:"+selectedIndId);
        System.out.println("C:"+selectedCollId);

        if(selectedObjectId != "-1" && selectedObjectId != null)
            selectedMeanList.add(meaningFacade.findOne(Integer.parseInt(selectedObjectId)));
        if(selectedTimeId != "-1" && selectedTimeId != null)
            selectedMeanList.add(meaningFacade.findOne(Integer.parseInt(selectedTimeId)));
        if(selectedPlaceId != "-1" && selectedPlaceId != null)
            selectedMeanList.add(meaningFacade.findOne(Integer.parseInt(selectedPlaceId)));
        if(selectedSituId != "-1" && selectedSituId != null)
            selectedMeanList.add(meaningFacade.findOne(Integer.parseInt(selectedSituId)));
        if(selectedIndId != "-1" && selectedIndId != null)
            selectedMeanList.add(meaningFacade.findOne(Integer.parseInt(selectedIndId)));
        if(selectedCollId != "-1" && selectedCollId != null)
            selectedMeanList.add(meaningFacade.findOne(Integer.parseInt(selectedCollId)));
    }

    public void create() {
        checkSelect();

        // Image作成
        imageFacade.create(image);
        image.setName(Integer.toString(image.getId()));
        imageFacade.update(image);
        uploadImage(file,"/image/",Integer.toString(image.getId()));

        // View Symbol作成
        viewSymbol.setName("test");
        viewSymbol.setImage(image);
        viewSymbol.setMultiviewSymbol(multiviewSymbol);
        viewSymbolFacade.create(viewSymbol);
        viewSymbol.setName(multiviewSymbol.getCaption()+"-"+viewSymbol.getId());
        viewSymbolFacade.update(viewSymbol);

        // View Symbolに注釈
        ViewSymbolHasMeaning viewSymbolHasMeaning;
        ViewSymbolHasMeaningId viewSymbolHasMeaningId;
        for(Meaning meaning : selectedMeanList) {
            viewSymbolHasMeaning = new ViewSymbolHasMeaning();
            viewSymbolHasMeaningId = new ViewSymbolHasMeaningId();
            viewSymbolHasMeaningId.setMeaningId(meaning.getId());
            viewSymbolHasMeaningId.setViewSymbolId(viewSymbol.getId());
            viewSymbolHasMeaning.setId(viewSymbolHasMeaningId);
            viewSymbolHasMeaningFacade.create(viewSymbolHasMeaning);
        }

        // View Symbol画像作成
        imgProc(getImg("/image/"+Integer.toString(image.getId())+".jpg"),
                "/vs/"+multiviewSymbol.getCaption()+"/s/", multiviewSymbol.getCaption()+"-"+Integer.toString(image.getId())+"_s", 200);
        imgProc(getImg("/image/"+Integer.toString(image.getId())+".jpg"),
                "/vs/"+multiviewSymbol.getCaption()+"/m/", multiviewSymbol.getCaption()+"-"+Integer.toString(image.getId())+"_m",500);
        imgProc(getImg("/image/"+Integer.toString(image.getId())+".jpg"),
                "/vs/"+multiviewSymbol.getCaption()+"/l/", multiviewSymbol.getCaption()+"-"+Integer.toString(image.getId())+"_l",800);

        init();
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

    // View Symbol画像作成
    private void imgProc(BufferedImage bufImg, String outputPath, String name, int size) {
        int w = bufImg.getWidth();
        int h = bufImg.getHeight();

        int SIZE = size;
        int width = SIZE,height = SIZE;

        BufferedImage squBufImg = null;
        if(w > h) {
            height = width * h / w;
            squBufImg = new BufferedImage(SIZE, height + size/6, BufferedImage.TYPE_INT_BGR);
        } else {
            width = size/6*5  * w / h;
            height = size/6*5;
            squBufImg = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_BGR);
        }

        AffineTransformOp xform = new AffineTransformOp( AffineTransform.getScaleInstance( ( double )width / w, ( double )height / h ), AffineTransformOp.TYPE_BILINEAR );
        BufferedImage dst = new BufferedImage(width, height, bufImg.getType() );
        xform.filter(bufImg, dst);

        int line = SIZE / selectedMeanList.size();
        int white = 255, black = 0, rgb = 0;
        if(width == SIZE) {
            for(int y = 0 ; y < height+size/6 ; y++) {
                for (int x = 0; x < width; x++) {
                    if(y == 0 || x == width-1 || y == height) rgb = 0xff000000 | black << 16 | black << 8 | black;
                    else if(y < height) {
                        if(x == 0) rgb = 0xff000000 | black << 16 | black << 8 | black;
                        else rgb = dst.getRGB(x,y);
                    } else {
                        if(x!= 0 && x%line==0) {
                            if(line*selectedMeanList.size() != x) {
                                rgb =  0xff000000 | black << 16 | black << 8 | black;
                            }
                        }
                        else if(x == 0 || x==width-1 || y==height+size/6-1) rgb =  0xff000000 | black << 16 | black << 8 | black;
                        else rgb = 0xff000000 | white << 16 | white << 8 | white;
                    }
                    squBufImg.setRGB(x,y,rgb);
                }
            }
        } else {
            int space = (size-width)/2;
            for(int y = 0 ; y < SIZE ; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if(y == 0) {
                        rgb =  0xff000000 | black << 16 | black << 8 | black;
                    } else if(y < size/6*5) {
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
                        if((x!= 0 && x%line==0) || y == size/6*5) {
                            if(line*selectedMeanList.size() != x) {
                                rgb =  0xff000000 | black << 16 | black << 8 | black;
                            }
                        }
                        else if(x == 0 || x==SIZE-1 || y==SIZE-1) rgb =  0xff000000 | black << 16 | black << 8 | black;
                        else rgb = 0xff000000 | white << 16 | white << 8 | white;
                    }
                    squBufImg.setRGB(x,y,rgb);
                }
            }
        }

        // アイコン追加
        int i = 1;
        int xStart = (size/selectedMeanList.size())/2-((int) (size/6*0.4));
        for(Meaning meaning : selectedMeanList) {
            if(i != 1) xStart += size/selectedMeanList.size();
            squBufImg = addIcon(xStart, (int) (size/6*0.8), squBufImg, getImg("/c_icon/" + meaning.getClarifier().getTypeName() + ".jpg"));
            i++;
        }

        // 一度byte配列変換
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream os = new BufferedOutputStream(baos);
        try {
            ImageIO.write(squBufImg,"jpg",os);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageInByte = baos.toByteArray();
        // byte配列をinputstreamに変換
        InputStream is = new ByteArrayInputStream(imageInByte);

        String filename = name + ".jpg";
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String filepath = ctx.getRealPath("resources");
        try {
            Files.copy(is, new File(filepath + outputPath, filename).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage addIcon(int xStart, int iconSize, BufferedImage squBufImg, BufferedImage icon) {
        AffineTransformOp form = new AffineTransformOp( AffineTransform.getScaleInstance( (double)iconSize/icon.getWidth(), (double)iconSize/icon.getHeight()), AffineTransformOp.TYPE_BILINEAR );
        BufferedImage dst = new BufferedImage(iconSize, iconSize, icon.getType());
        form.filter(icon, dst);

        int rgb = 0, xx = 0, yy = 0;

        // 所定の位置にアイコン上書き
        int yStart = squBufImg.getHeight() - iconSize/8*9;
        for(int y = yStart ; y < yStart +iconSize ; y++) {
            xx = 0;
            for(int x = xStart ; x < xStart+iconSize; x++) {
                rgb = dst.getRGB(xx,yy);
                squBufImg.setRGB(x,y,rgb);
                xx++;
            }
            yy++;
        }

        return squBufImg;
    }
}
