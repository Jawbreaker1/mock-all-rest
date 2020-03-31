class Day {

    private int currentDay = 0;
    private int totalAmountofDeadPeople = 0;
    private int totalAmountofEffectedPeople = 0;
    private int totalAmountOfRecoveredPeople = 0;
    private int recoveredPeopleToday = 0;
    private int nrOfIVEinUse = 0;
    private double dailySpreadRate = 0;
    //private double deathRate = 0;
    private int peakEffectedPeople = 0;
    private double IVARate = 0;
    private int maxIVA = 0;
    private double originalDeathRate = 0;
    private double currentDeathRate = 0;
    private int maxEffectedPeople = 0;
    private boolean peakFlag = false;
    private boolean spreadPeak = false;
    private int currentAmountofEffectedPeople = 0;
    private boolean hasIVEUse=false;
    private int deadPeopleToday=0;
    private int contaminatedPeopleToday = 0;
    private int nrOfIVEToday = 0;

    public Day(int currentDay, int totalAmountofEffectedPeople, double dailySpreadRate, double deathRate, int peakEffectedPeople, double IVArate, int maxIVA, int maxEffectedPeople, int currentAmountofEffectedPeople ){
        this.totalAmountofEffectedPeople = totalAmountofEffectedPeople;
        this.currentAmountofEffectedPeople = currentAmountofEffectedPeople;
        this.currentDay = currentDay;
        this.dailySpreadRate = dailySpreadRate;
        this.originalDeathRate = deathRate;
        this.currentDeathRate = deathRate;
        this.peakEffectedPeople = peakEffectedPeople;
        this.IVARate = IVArate;
        this.maxIVA = maxIVA;
        this.maxEffectedPeople = maxEffectedPeople;
        if (currentDay == 0){
            contaminatedPeopleToday = totalAmountofEffectedPeople;
        }
    }

    private void increaseDay(){
        currentDay++;
    }

    private int getCurrentDay(){
        return currentDay;
    }

    public void updateDay(Day relevantDay){
        //räkna upp vecka
        increaseDay();
        //räkna upp antal smittade
        increaseContaminated(relevantDay);
        //räkna upp antal friskförklarade
        increaseHealthy(relevantDay);
        //räkna upp antal döda
        increaseDeathCount(relevantDay);
        //räkna upp antal med intensivbehov
        calculateIVA(relevantDay);
    }

    private void increaseContaminated(Day relevantDay){
        //behöver veta potentiella totalen? Vi räknar upp med weekly spread tills dess vi nått hälften av totalen
        //vad gör vi när vi nått peak? Halvera spridningen?
        if (totalAmountofEffectedPeople>=(maxEffectedPeople/2) /*&& spreadPeak == false*/) {
            dailySpreadRate = (int)Math.round(dailySpreadRate/2);
            spreadPeak = true;
            //System.out.println("Half expected population contaminated!! Reduced spread rate.");
        } else if (totalAmountofEffectedPeople >= maxEffectedPeople) {
            dailySpreadRate = 0;
        }


        contaminatedPeopleToday = (int)Math.round((currentAmountofEffectedPeople * (dailySpreadRate / 100)));

        if (relevantDay!=null) {
            totalAmountofEffectedPeople = totalAmountofEffectedPeople + (int)Math.round((currentAmountofEffectedPeople * (dailySpreadRate / 100))) + getDeadPeopleToday();
            currentAmountofEffectedPeople = currentAmountofEffectedPeople + (int)Math.round((currentAmountofEffectedPeople * (dailySpreadRate / 100))) - relevantDay.getContaminatedPeopleToday() - getDeadPeopleToday();
            if (currentAmountofEffectedPeople < 0){
                currentAmountofEffectedPeople = 0;
            }

        } else
        {
            totalAmountofEffectedPeople = totalAmountofEffectedPeople + (int)Math.round((currentAmountofEffectedPeople * (dailySpreadRate / 100)));
            currentAmountofEffectedPeople = currentAmountofEffectedPeople + (int)Math.round((currentAmountofEffectedPeople * (dailySpreadRate / 100)));
        }

    }

    public int getDay(){
        return currentDay;
    }

    private void increaseHealthy(Day relevantDay){
        if (relevantDay!=null) {
           /* if(relevantDay.getTotalAmountofEffectedPeople() > currentAmountofEffectedPeople && peakFlag == false) {
                System.out.println("Peak contamination reached!!!!");
                peakFlag = true;
            }*/
            totalAmountOfRecoveredPeople = totalAmountOfRecoveredPeople + relevantDay.getContaminatedPeopleToday() - getDeadPeopleToday();
            recoveredPeopleToday = relevantDay.getContaminatedPeopleToday() - getDeadPeopleToday();
        }
    }

    private boolean isIVAFull(){
        return nrOfIVEinUse>=maxIVA;
    }

    private void increaseDeathCount(Day relevantDay){
        //kolla IVA platser. Om fullt avlider alla allvarligt sjuka utöver dessa efter 2v
        //lägg till deathrate * alla insjuknade för 2 v sen
        if (relevantDay!=null) {
            deadPeopleToday = (int)Math.round((relevantDay.getContaminatedPeopleToday() * (currentDeathRate / 100)));
            totalAmountofDeadPeople = totalAmountofDeadPeople + deadPeopleToday;
        }

    }

    private void calculateIVA(Day relevantDay) {
        if (relevantDay!=null) {
            nrOfIVEinUse = nrOfIVEinUse - relevantDay.getNrOfIVAToday();
            nrOfIVEToday = (int)Math.round(relevantDay.getContaminatedPeopleToday() * (IVARate / 100));

            if ((nrOfIVEToday + nrOfIVEinUse) >= maxIVA) {
                nrOfIVEToday = maxIVA - nrOfIVEinUse;
            }

            nrOfIVEinUse = nrOfIVEinUse + nrOfIVEToday;



            //System.out.println("IVA actually needed: " + nrOfIVEinUse);
            if (isIVAFull()) {
                nrOfIVEinUse = maxIVA;
                currentDeathRate = IVARate;
            } else {
                currentDeathRate = originalDeathRate;
            }




        }
    }

    public int getCurrentAmountofEffectedPeople(){
        return currentAmountofEffectedPeople;
    }

    public int getTotalAmountOfRecoveredPeople(){
        return totalAmountOfRecoveredPeople;
    }

    public void setTotalAmountOfRecoveredPeople(int nr){
        totalAmountOfRecoveredPeople = nr;
    }
    public int getTotalAmountofDeadPeople(){
        return totalAmountofDeadPeople;
    }

    public void setTotalAmountofDeadPeople(int nr){
        totalAmountofDeadPeople = nr;
    }

    public int getTotalAmountofEffectedPeople() {
        return (int)Math.round(totalAmountofEffectedPeople);
    }

    public int getNrOfIVEinUse(){
        return nrOfIVEinUse;
    }

    public void setNrOfIVEinUse(int nr){
        nrOfIVEinUse = nr;
    }

    public void setDeadPeopleToday(int nr) {
        deadPeopleToday = nr;
    }

    public int getDeadPeopleToday(){
        return deadPeopleToday;
    }

    public int getContaminatedPeopleToday(){
        return contaminatedPeopleToday;
    }

    public int getRecoveredPeopleToday() { return recoveredPeopleToday; }

    public void setContaminatedPeopleToday(int nr) {
        contaminatedPeopleToday = nr;
    }

    public int getNrOfIVAToday() {
        return nrOfIVEToday;
    }

    public void setNrOfIVEToday(int nr) {
        nrOfIVEToday = nr;
    }

    public void setCurrentDeathRate(double nr){
        currentDeathRate = nr;
    }

    public void setSpreakPeak(boolean peak) {
        spreadPeak = peak;
    }

    public Day getCopyOfDay(){
        Day copyOfDay = new Day(currentDay, totalAmountofEffectedPeople, dailySpreadRate, currentDeathRate, peakEffectedPeople, IVARate, maxIVA, maxEffectedPeople, currentAmountofEffectedPeople);
        copyOfDay.setNrOfIVEinUse(nrOfIVEinUse);
        copyOfDay.setDeadPeopleToday(deadPeopleToday);
        copyOfDay.setContaminatedPeopleToday(contaminatedPeopleToday);
        copyOfDay.setCurrentDeathRate(currentDeathRate);
        copyOfDay.setTotalAmountOfRecoveredPeople(totalAmountOfRecoveredPeople);
        copyOfDay.setTotalAmountofDeadPeople(totalAmountofDeadPeople);
        copyOfDay.setSpreakPeak(spreadPeak);
        copyOfDay.setNrOfIVEToday(nrOfIVEToday);
        return copyOfDay;
    }
}
