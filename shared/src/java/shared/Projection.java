package shared;

import java.util.ArrayList;
import shared.geometrical.*;

public class Projection{
  private ArrayList<Segment> parts;

  public Projection(ArrayList<Segment> parts){
    setParts(parts);
  }

  public ArrayList<Segment> getParts(){
    return this.parts;
  }

  /** Sets the segments to the partition
   *  sorts them by lower x then lower y 
   */
  public void setParts(ArrayList<Segment> parts){
    parts.sort((a,b)->{
      int comp = Double.compare(a.getStart().x, b.getStart().x);
      if(comp == 0)
        return Double.compare(b.getStart().y,a.getStart().y);
      return comp;
    });
    this.parts = parts;
  }

  /** merges the part of the specified Projection in the current one 
 * @param other the Projection to be merged
   */
  public void mergePartsFrom(Projection other){
    if (other == null){  
      return;}
    ArrayList<Segment> merged = new ArrayList<>();
    ArrayList<Segment> toMerge = other.getParts();
    if (toMerge.size() == 0){
      return;
    }
    if(this.parts == null){
      this.parts = toMerge;
    }
    int j = 0;

    for (int i = 0; i < toMerge.size(); i++) {
      Segment seg = toMerge.get(i);
      while(j<this.parts.size() 
          && ((!seg.isVertical() 
          && DoubleUtils.lowerOrEqual(parts.get(j).getEnd().x, seg.getStart().x)) || (seg.isVertical() && DoubleUtils.lowerOrEqual(seg.getStart().y,parts.get(j).getEnd().y)) )){ 
        
        if (!this.parts.get(j).getStart().equals(this.parts.get(j).getEnd()))
          merged.add(this.parts.get(j));
        j++;        
      }
      if(j< this.parts.size()){
        if(!seg.isVertical() && this.parts.get(j).getStart().x < seg.getStart().x){
          Segment temp = new Segment(this.parts.get(j).getStart().x,this.parts.get(j).getStart().y,seg.getStart().x,seg.getStart().y,this.parts.get(j).getColor());
          if(!temp.getStart().equals(temp.getEnd()))
            merged.add(temp);
        }
        if(seg.isVertical() && seg.getStart().y < parts.get(j).getStart().y){
          Segment temp = new Segment(this.parts.get(j).getStart().x,this.parts.get(j).getStart().y,seg.getStart().x,seg.getStart().y,this.parts.get(j).getColor());
          if(!temp.getStart().equals(temp.getEnd()))
            merged.add(temp);
        }
      }
      if(!seg.getStart().equals(seg.getEnd()))
        merged.add(seg);
      while( j<this.parts.size() && ((!seg.isVertical() && parts.get(j).getEnd().x<seg.getEnd().x) || (seg.isVertical() && parts.get(j).getEnd().y > seg.getEnd().y))){
        j++;
      }
      if(j < this.parts.size()){
          if ((!seg.isVertical() && parts.get(j).getStart().x < seg.getEnd().x) || (seg.isVertical() && parts.get(j).getStart().y > seg.getEnd().y) ){
            parts.get(j).setStart(seg.getEnd());        
          }
          if(i == toMerge.size()-1){
            if(!this.parts.get(j).getStart().equals(this.parts.get(j).getEnd()))
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


  /** Fatten the projection to y=0 and x=fraction of range 
   * @param range the maximum x value to flatten to 
   */
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
