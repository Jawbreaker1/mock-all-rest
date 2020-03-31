import java.util.ArrayList;

class Calculator {

    private int totalExpectedAmountofEffetedPeople;

    private ArrayList<Day> daysOfInfectionList;
    private int daysOfIllness;

    public Calculator(int population, int initialAmountOfInfected, double dailySpreadRate, double expectedPopulationCoverage, double deathRate,  double percetageInNeedOfIVE, double percentageInNeedOfMedicalCare, int maxIVA, int daysOfIllness){
        setTotalExpectedAmountofEffectedPeople(population, expectedPopulationCoverage);
        this.daysOfIllness = daysOfIllness;
        Day day = new Day(0, initialAmountOfInfected, dailySpreadRate, deathRate, totalExpectedAmountofEffetedPeople/2, percetageInNeedOfIVE, maxIVA, totalExpectedAmountofEffetedPeople,initialAmountOfInfected );
        //kör calculateDay fram tills dess att vi har noll sjuka och noll IVA.
        daysOfInfectionList = new ArrayList<Day>();
        while(day.getCurrentAmountofEffectedPeople()>0 || day.getNrOfIVEinUse()>0){
            calculateDay(day);
        }
    }

    private void setTotalExpectedAmountofEffectedPeople(int population, double expectedPopulationCoverage){
        totalExpectedAmountofEffetedPeople = (int)Math.round(population * (double)(expectedPopulationCoverage/100));

    }

    private int geTotalExpectedAmountofEffectedPeople(){
        return totalExpectedAmountofEffetedPeople;
    }

    private void calculateDay(Day day){
        Day copyOfDay = day.getCopyOfDay();

        if(daysOfInfectionList.size()>=daysOfIllness && (daysOfInfectionList.get(daysOfIllness - 1)!=null) ) {
            day.updateDay(daysOfInfectionList.get(daysOfIllness -1));
        } else {
            day.updateDay(null);
        }

        addDayToThreeWeeks(copyOfDay);

        System.out.println("START*************************************************************");
        System.out.println("DAY: " + day.getDay());
        System.out.println("Contaminated today: " + day.getContaminatedPeopleToday());
        System.out.println("Total Contaminated population: " + day.getCurrentAmountofEffectedPeople());
        System.out.println("Total effected population (Sick and Recovered): " + day.getTotalAmountofEffectedPeople());
        System.out.println("IVA: " + day.getNrOfIVEinUse());
        System.out.println("Deaths today: " + day.getDeadPeopleToday());
        System.out.println("Total Deaths: " + day.getTotalAmountofDeadPeople());
        System.out.println("Recovered People Today: " + day.getRecoveredPeopleToday());
        System.out.println("Total Recovered People: " + day.getTotalAmountOfRecoveredPeople());
        //System.out.println(day.getCurrentAmountofEffectedPeople()+ "; ");
        //kopiera undan föregående och flytta förr-förra veckan
        //uppdatera all data i en aktuell week
        //förr-förra veckans data används för att räkna IVA-behov, friskförklara och dödsfall
        //om vi uppnått maximal spridning så får det hanteras här när man uppdaterar veckan
        //skriv ut veckansdata via rapportklassen
    }


    private void addDayToThreeWeeks(Day day) {
        ArrayList<Day> newThreeWeeks = new ArrayList<Day>();

        for (int counter = 0; counter < daysOfInfectionList.size(); counter++) {
            newThreeWeeks.add(null);
        }

        for (int counter = 0; counter < daysOfInfectionList.size(); counter++) {
            newThreeWeeks.set(counter, daysOfInfectionList.get(counter));
        }
        newThreeWeeks.add(0, day);
        daysOfInfectionList = newThreeWeeks;
    }
}



