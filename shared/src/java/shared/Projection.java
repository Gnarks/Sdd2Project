package shared;

import java.util.ArrayList;
import shared.geometrical.*;

public class Projection{
  private ArrayList<Segment> parts;

  public Projection(ArrayList<Segment> parts){
    parts.sort((a,b)->{
      return Double.compare(a.getStart().x, b.getStart().x);
    });
    this.parts = parts;
  }

  public ArrayList<Segment> getParts(){
    return this.parts;
  }

  public void setParts(ArrayList<Segment> parts){
    parts.sort((a,b)->{
      return Double.compare(a.getStart().x, b.getStart().x);
    });
    this.parts = parts;
  }

  public void mergeParts(Projection segments){
    if (segments == null){  
      return;}

    ArrayList<Segment> merged = new ArrayList<>();
    ArrayList<Segment> toMerge = segments.getParts();
    Utils.sortSegments(toMerge);
    if (toMerge.size() == 0){
      return;
    }
    if(this.parts == null){
      this.parts = toMerge;
    }
    int j = 0;

    for (int i = 0; i < toMerge.size(); i++) {
      Segment seg = toMerge.get(i);
      while(j<this.parts.size() && parts.get(j).getEnd().x <= seg.getStart().x && (!Utils.areEqual(this.parts.get(j).getStart(),this.parts.get(j).getEnd()))) {
        merged.add(this.parts.get(j));
        j++;        
      }
      if(j< this.parts.size()){
        if(this.parts.get(j).getStart().x < seg.getStart().x){
          Segment temp = new Segment(this.parts.get(j).getStart().x,this.parts.get(j).getStart().y,seg.getStart().x,seg.getStart().y,this.parts.get(j).getColor());
            merged.add(temp);

        }
      }
      merged.add(seg);
      while( j<this.parts.size() && parts.get(j).getEnd().x<seg.getEnd().x){
        j++;
      }
      if(j < this.parts.size()){
          if (parts.get(j).getStart().x<seg.getEnd().x){
            parts.get(j).setStart(seg.getEnd());        
          }
          if((i == toMerge.size()-1) && (!Utils.areEqual(this.parts.get(j).getStart(),this.parts.get(j).getEnd()))){
            merged.add(this.parts.get(j));
          }
      }
    }
    this.setParts(merged);
  }

  public String toString(){
    return parts.toString();
  }
}
