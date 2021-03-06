package dictionary.entity;
// Generated 2018/11/20 23:16:32 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ViewSymbolHasMeaningId generated by hbm2java
 */
@Embeddable
public class ViewSymbolHasMeaningId  implements java.io.Serializable {


     private int viewSymbolId;
     private int meaningId;

    public ViewSymbolHasMeaningId() {
    }

    public ViewSymbolHasMeaningId(int viewSymbolId, int meaningId) {
       this.viewSymbolId = viewSymbolId;
       this.meaningId = meaningId;
    }
   


    @Column(name="view_symbol_id", nullable=false)
    public int getViewSymbolId() {
        return this.viewSymbolId;
    }
    
    public void setViewSymbolId(int viewSymbolId) {
        this.viewSymbolId = viewSymbolId;
    }


    @Column(name="meaning_id", nullable=false)
    public int getMeaningId() {
        return this.meaningId;
    }
    
    public void setMeaningId(int meaningId) {
        this.meaningId = meaningId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ViewSymbolHasMeaningId) ) return false;
		 ViewSymbolHasMeaningId castOther = ( ViewSymbolHasMeaningId ) other; 
         
		 return (this.getViewSymbolId()==castOther.getViewSymbolId())
 && (this.getMeaningId()==castOther.getMeaningId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getViewSymbolId();
         result = 37 * result + this.getMeaningId();
         return result;
   }   


}


