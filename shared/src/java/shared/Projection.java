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
      int comp = Double.compare(a.getStart().x, b.getStart().x);
      if(comp == 0)
        return Double.compare(b.getStart().y,a.getStart().y);
      return comp;
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
      while(j<this.parts.size() && Utils.lowerOrEqual(parts.get(j).getEnd().x, seg.getStart().x) && (!Utils.areEqual(this.parts.get(j).getStart(),this.parts.get(j).getEnd()))) {
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
            j++;
          }
      }
    }
    while( j<this.parts.size()){
        merged.add(this.parts.get(j));
        j++;
      }
    this.setParts(merged);
  }

  public void flatten(double range){
    if(this.parts == null || this.parts.size() == 0)
      return;
    ArrayList<Segment> flat = new ArrayList<>();
    int n = this.parts.size();
    Point startParts = this.parts.get(0).getStart();
    Point endParts = this.parts.get(n-1).getEnd();
    double scaling = (2*range)/startParts.distanceTo(endParts);
    
    for (int i = 0; i < n; i++) {
      Segment seg = this.parts.get(i);
      double lenSeg = seg.getStart().distanceTo(seg.getEnd())*scaling;
      double distanceStartSeg = startParts.distanceTo(seg.getStart())*scaling;
      flat.add(new Segment(distanceStartSeg-range,0,distanceStartSeg+lenSeg-range,0,seg.getColor()));
    }
    this.parts = flat;
  }

  public String toString(){
    return parts.toString();
  }
}
