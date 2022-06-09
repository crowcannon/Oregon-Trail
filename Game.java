/*
Game class - survival game

In this game, the player character attempts to survive the Oregon trail. The player wants at least one member of their party to be alive by the end of a set amount of days. Certain events may cause the journey to be longer than intended, and certain events may cause party members to die depending on the player's decision. If all party members die, the player loses the game.

Authors - Nora Fotoohi, Laura Zhang

Change history:
5/13: Class design created
5/16: Made classes and commands
5/17: Finished starting classes and commands for party
5/19: Started on the actual game design
5/20: Created hashmap and person class
5/31: Created a try/catch statement
6/1: Added problem randomizer, added choices for problems & Laura worked hard 
6/2: Game pretty much finished, added death system win if reach end of days with one or more people, lose if all people die before end of days
6/7: Added serialization to record most days travelled
6/8: Added total losses to serialization - now you can know how many people died under your "profile"! Made code neater, added switch statement that allows the game to be played multiple times in one run.
*/

/*
State info

//  Arraylist stores party members
private static ArrayList<String> party

//  Hashmap stores sickness and wagon afflictions and gives a y/n choice if they want to fix it.
private Hashmap<String, String> dailyProblems;
private Hashmap<String, String> wagonProblems;

//  Static int stores the current day, which progresses one at a time
private int day;

//  Static int stores the number of people who die
private int losses;

Method info

//  Input names allows for party creation and sets the number of members that will be travelling.

//  startGame, loops until the number of iterations matches the number of days the player inputted at the beginning. If i matches day and at least one player is alive, game ends (win). If player status is dead regardless of daysLeft, game ends (lose). Certain choices will add to the number of days the player will have to travel

*/

import java.util.HashMap;
import java.util.ArrayList;
//import java.util.Math;

public class Game {
  //  Private instance variables to store the party
  private ArrayList<String> party;

  //  Constructor, to initialize the instance variable
  public Game () {
   //stats = new HashMap<String, String>();
    party = new ArrayList<String>();
  }

  //  Starts game, serialization takes place here
  public void play () {
    System.out.println ("\nWelcome to Oregon Trail, where you try to survive");

      String name = Utils.inputStr ("\nWhat's your name? ").trim();
      State s = State.restore(name);
      if (s != null) {
        System.out.println ("\nWelcome back, " + name + "!\n" + s);
        //  continue;
      } else {
        s = new State();
        s.name = name;
      }
    System.out.println("\nLet's create a party!");
    inputNames();
    //int rec = startGame();
    //if (rec > s.recordDays) {
    //s.recordDays = rec;
    //}
    //s.save();
    
    ArrayList<Integer> rec = startGame();
    
    if (rec.get(0) > s.recordDays) {
      s.recordDays = rec.get(0);
    }
      //System.out.println(rec.get(0));
      s.totalDeaths += rec.get(1);
      if (rec.get(2) == 0) {
        s.succ++;
      } else {
        s.unSucc++;
      }
      s.save();    
  }

  //  Allows user to edit party - add and delete names, start game
  public void inputNames() {
    System.out.println ("You can: \nv-view all members, \na-add a member, \nd-delete a member, \nq-quit recording members and start the journey");
    while (true) {
      String command = Utils.inputStr("\nWhat do you want to do? ");
      switch (command) {
        case "v":  //  View all members
          if ((party == null)||(party.isEmpty())) {
            System.out.println ("Party has not been set yet.");
          } else {
          for (String partyKey : party)
            System.out.println (partyKey);
            }
          break;       
          
        case "a":  //  Add a member
          System.out.println ("Name your member. For example, 'Andy'");
          String member = Utils.inputStr ("Who is your member? ");
          party.add(member);
          break;
          
        case "d":  //  Delete a member
          member = Utils.inputStr ("What member do you want to delete? ");
          party.remove (member);
          break;
          
        case "q":  //  Quit
          //  System.out.println(party.size());
          if(party.size()<=1) {
          System.out.println("Not enough members. There must be at least 2");
          } else {
              System.out.println ("Done recording members\n\n\n-----------------------------------------------------------\n\n");
              return;
            }
          break;

        default:  //  Requests valid command
          System.out.print ("Please type one of the commands\n ");
//System.out.println (stats.get(command));
      }
    }
  }  

  //  All gameplay functions take place here
  public ArrayList<Integer> startGame() {
    ArrayList<Integer> fin = new ArrayList<Integer>();
    HashMap<String, String> dailyProblems = new HashMap<String, String>();
    HashMap<String, String> wagonProblems = new HashMap<String, String>();
    
    dailyProblems.put("measles", "Cure traveller?\ny-yes\nn-no");
    dailyProblems.put("typhoid fever", "Cure traveller?\ny-yes\nn-no");
    dailyProblems.put("smallpox", "Cure traveller?\ny-yes\nn-no");
    dailyProblems.put("a broken leg", "Leave traveller behind?\nThey will only slow you down\ny-yes\nn-no");
    
    wagonProblems.put("Ox ran away", "Purchase new one?\ny-yes\nn-no");
    wagonProblems.put("Broken wheel", "Fix it?\ny-yes\nn-no");
    //wagonProblems.put("Broken yoke", "Fix it?\ny-yes\nn-no");
    wagonProblems.put("Ox is injured", "Heal ox?\ny-yes\nn-no");
    //wagonProblems.put("Ox is sick", "Heal ox?\ny-yes\nn-no");
    
    System.out.println("You and your party have to travel the Oregon trail.\nYour goal is to reach  the end of your journey with at least one party member.");
    
    int day = Utils.inputNum("\nEnter the number of days you wish to travel.\nThey must be between 7 and 10.");
    int losses = 0;
    
    System.out.println("You wish to travel for " + day + " days.");

    //  Get the keys of the `HashMap` returned as an array of Strings
    String[] keys = dailyProblems.keySet().toArray(new String[0]);
    String[] keys2 = wagonProblems.keySet().toArray(new String[0]);
    
    for (int i = 1; i <= day; i++) {
      System.out.println("\nDay " + i);
      if ((i == day)&&(party.size()!=0)) {
        System.out.print("You made it to the end!\n" + losses + " loss(es)\nGood job");
        fin.add(day);
        fin.add(losses);
        fin.add(0);
        return fin;
      } else if (party.size()!=0) {
        boolean problem = (Utils.randInt(0,1)==0);
        System.out.println(party.size() + " member(s) in party");
        if (problem) {
          System.out.println("2 problems");
          
      //  To pick one at random
      String key = Utils.randChoice(keys);
      String key2 = Utils.randChoice(keys2);
      int randIndex = ((int)(Math.random() * party.size()));
      String afflicted = (party.get(randIndex));
      System.out.println(afflicted + " has " + key);
      int two = 2;
      while(two > 0) {
        if (two == 2) {
          String command = Utils.inputStr(dailyProblems.get(key)+"\n");
          switch (command) {      
            case "y":  //  Fix
              if(dailyProblems.get(key)=="Leave traveller behind?\nThey will only slow you down\ny-yes\nn-no") {
                  System.out.print("You made the right choice.\n" + afflicted + " was removed from the party\n");
                  party.remove(randIndex);
                  losses++;
                } else {
                  System.out.print("Because the problem was resolved,\nthe journey continued without any delay\n");
                }
                two--;                  
                break;
            case "n":  //  Don't fix
              day++;
              if(dailyProblems.get(key)=="Leave traveller behind?\nThey will only slow you down\ny-yes\nn-no") {
                System.out.print("Supporting the injured traveller \nincreased the duration of your journey by one day.\n");
              } else {
                System.out.print(afflicted + " has succumbed to " + key + "\n");
                party.remove(randIndex);
                losses++;
                }
                two--;
                break;
      
              default:  //  if typed anything else besides y or n
                System.out.print ("Please type y or n\n");
              break;
          }
        } else if (two == 1) {
            System.out.println("\n" + key2);
            String command = Utils.inputStr(wagonProblems.get(key2)+"\n");
            switch (command) {   
              case "y":  //  Fix
                System.out.print("Because the problem was resolved,\nthe journey continued without any delay\n");
                two--;
                break;
        
              case "n":  //  Don't fix
                day++;
                System.out.print("Due to complications,\nyour journey has been extended by one day\n");
                two--;
                break;
        
              default:  //  If typed anything else besides y or n
                System.out.print ("Please type y or n\n");
                break;
              }
            }
          }
          //get keys into list and pick random from array
          //Object[] values = dailyProblems.values().toArray();
        } else {  //  One problem
          System.out.println("1 problem");
          String key2 = Utils.randChoice(keys2);
          //String key = Utils.randChoice(key);
          int one = 1;
          while(one > 0) {
            //System.out.println(wagonProblems.size());
            System.out.println(key2);
            String command = Utils.inputStr(wagonProblems.get(key2)+"\n");
            switch (command) {
              
            case "y":  //  Fix
              System.out.print("Because the problem was resolved,\nthe journey continued without any delay\n");
              //wagonProblems.remove(key2);
              //key2 = //Utils.randChoice(wagonProblems.keySet().toArray(new String[0]));
              one--;
              break;
            case "n":  //  Don't fix
              day++;
              System.out.print("Due to complications,\nyour journey has been extended by one day\n");
              one--;
              break;
            default:  // Look up a specific fact (the "command")
              System.out.print ("Please type y or n\n");
              break;
            }
          }
        }
      } else {
        System.out.println("Your party has perished, you lose the game :(");
        fin.add(day);
        fin.add(losses);
        fin.add(1);
        return fin;
      }
    }
    return fin;
  }
}