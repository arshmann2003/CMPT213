import java.util.List;

public class Main {
    public static void main(String[] args) {
        StoreMinions dataBase = new StoreMinions();
        UI ui = new UI();
        ui.welcomeMessage();
        while(true){
            ui.displayMenu();
            int chosenMenuOption = ui.getMenuChoice();

            if(chosenMenuOption==1){
                dataBase.displayMinions();
            }
            else if(chosenMenuOption==2){
                List<Object> data = ui.getNewMinionInfo();
                dataBase.addMinion(data);
            }
            else if(chosenMenuOption==3){
                dataBase.displayMinions();
                int i = ui.getChosenIndex(dataBase.getNumberOfMinions());
                dataBase.removeMinionAtIndex(i);
            }
            else if(chosenMenuOption==4) {
                if(dataBase.getNumberOfMinions() > 0) {
                    dataBase.displayMinions();
                    int i = ui.getChosenIndex(dataBase.getNumberOfMinions());
                    if(i!=-1){
                        Minion min = dataBase.getMinionAtIndex(i);
                        dataBase.getMinionAtIndex(i).incrementEvilDeeds();
                        ui.displayEvilDeedMessage(min);
                    }
                }
            }
            else if(chosenMenuOption==5){
                ui.displayMinionsToString(dataBase);
            }
            else if(chosenMenuOption==6){break;}
        }
    }
}