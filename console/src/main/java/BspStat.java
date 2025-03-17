public class BspStat {

  public double meanBspCpuTime;
  public double meanPainterCpuTime;
  public double meanSize;
  public double meanHeight;


  public BspStat(double meanBspCpuTime, double meanPainterCpuTime, double meanSize, double meanHeight){
    this.meanSize = meanSize;
    this.meanHeight = meanHeight;
    this.meanBspCpuTime = meanBspCpuTime;
    this.meanPainterCpuTime = meanPainterCpuTime;
  }
}
