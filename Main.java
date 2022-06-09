/*
Main - this is a runner class that runs the Game class

Author - Marc Shepard
*/

class Main {
  public static void main(String[] args) {
    Game g = new Game();
    g.play();

  while (true) {
    String command = Utils.inputStr("\nPlay again?\ny-yes\nn-no\n");
    switch (command) {
      case "y":  // yes
        g.play();
        break;
        
      case "n":  // no
        System.out.println("\nThanks for playing! Bye now :)");
        return;
         
      default:  // if typed anything else besides y or n
        System.out.println ("Please type y or n\n");    
      }
  }
    //testUtils();
    
  }

  // Unit tests for Utils.java. Call this from main if you want to
  // see examples of the Utils methods
//   public static void testUtils () {
//     String s = Utils.inputStr ("What's your name? ");
//     System.out.println ("Hi " + s);
    
//     int n = Utils.inputNum ("What's your age? ");
//     System.out.println ("You typed " + n);

//     int r = Utils.randInt (1, 100);
//     System.out.println ("A random number between 1-100 is " + r);

//     String[] strs = {"chocolate", "vanilla", "strawberry"};
//     s = Utils.randChoice(strs);
//     System.out.println ("A random flavor is: " + s);
//   }
}