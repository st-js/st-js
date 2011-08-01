package test;
 
import static org.stjs.javascript.JSNumberAdapter.toFixed;

public class AssetAllocation {

  public long fixedIncome(int age, int risk, long lnw, boolean hasGoal, long iliqSec) {
    long alloc = age-(10*(risk-3));
    if(lnw > 5000000 || iliqSec > 1000000) alloc -= 10;
    if(hasGoal) alloc += 10;
    if(alloc < 0) return 0;
    if(alloc > 100) return 100;
    return alloc;
  }
  
  public long resourceAndRealEstate(long fixedIncome, long remainder) {
    long allocation = fixedIncome > 50 ? 7 : 10;
    return (allocation * 2 > remainder) ? remainder/2 : allocation;
  }
  
  public long privateInvestments(long fixedIncome, int risk, long lnw, long remainder) {
    long allocation = 0;
    if(risk < 3 || lnw < 1500000) return allocation;
    allocation += lnw < 10000000 ? 10 : 20;
    allocation += risk == 5 ? 10 : 0;
    return allocation > remainder ? remainder : allocation;
  }
  
  public PublicEquities publicEquities (long remainder) {
    String us;
    String foreign;
    String emerging;
    if(remainder < 1) {
      us=foreign=emerging="0";
    } else {
      us = toFixed(remainder * .5, 1);
      foreign = toFixed(remainder * .33, 1);
      emerging = toFixed(remainder * .17, 1);
    }
    return new PublicEquities(us, foreign, emerging);
  }
  

  static class PublicEquities {
    String us;
    String foreign;
    String emerging;
    
    public PublicEquities(String us, String foreign, String emerging) {
      this.us = us;
      this.foreign = foreign;
      this.emerging = emerging;
    }
    
  }
}
