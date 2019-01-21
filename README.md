# MKS21X-FinalProject

INSTRUCTIONS (README!!!!!!!!!!!!!)=============================================
To begin the game...
- Resize the terminal to be at least 67 x 35 size (as in 68 across and 35 down) so the visuals will work correctly.
- To compile, type in 'javac -cp lanterna.jar:. TerminalClass.java'
- The command line to begin the game from the terminal is 'java -cp lanterna.jar:. TerminalClass'
- If you want to go into "dieRollManipulate" mode, a mode that allows you to control the die rolls you receive, 
  then do 'java -cp lanterna.jar:. TerminalClass dieRollManipulate' in the terminal instead. 
  
OR

- Type in the terminal './run.sh'
- To get the dieRollManipulate mode, type './dieRoll.sh'

- Instructions for navegating around the main menu is already printed on the terminal when starting up the game.
- Press Enter to roll the dice.
    -If in dieRollManipulate mode, you have to Press Tab immediately after this to increment the number 
     of the die roll (goes from 1 to # of max die roll selected at main menu. This max die roll is defaulted to 6 in the main menu).
    -Then press Enter again to finalize that die roll. You will then be allowed to swap between planes allowed by that die roll.
    -If not in dieRollManipulate, Tab will not increment die rolls.
- Press Tab to swap between the planes you are allowed to select that turn. Press Enter to select that plane to move.
- Press Enter again to roll the dice.
- Press Escape to exit the game (WARNING: Games cannot be saved).
- If in dieRollManipulate mode, you can press lowercase f to make all the planes finished to skip right to the win screen.

PLEASE.
DO.
NOT.
BUTTON.
MASH.
It doesn't break the game, but the game takes button mashing the Enter key as a valid input even though it shouldn't.

GAME DESCRIPTION ==============================================================

Description:
- So this game is called Aeroplane Chess, and it is a popular Chinese board game. 
- How it works is that there are up to a maximum of four players. We implemented a feature where you can select the # of players you want (a max of 4 and a min of 2).
- Each of the four corners of the board represents a different player and their respective color (as indicated by the colors of the letter 'P'.
- Each 'P' represents a separate plane, with each player having a total of four planes. 
- The corners are called "hangars", because planes are at home when they are in hangars. 
- To get a plane out of a hangar, you need to roll a die, and that die must give an even number, otherwise your turn is skipped. 
- If one gets an even number, then they choose which plane in the hangar to go to what we call the "launchingTile" which is basically a tile where the plane is located before officially trekking onto the circular-ish board. 
- Once on the launchingTile, when it is your turn again, you must roll the die again to get that plane onto the board.
- 'T' represents a tile on the board. Once on the board, the number of tiles you travel depends on your dieRoll. 
- Planes move clockwise around the board. Planes have to make a whole revolution around the track in order to go to the tiles in the middle.
- Once in the middle, you travel towards the exact four tiles in the center and once you reach one of those, the plane that has reached the middle will be recorded as “finished”. 
- Once the plane is finished, then it goes back to the hangar and is marked as finished by a black background color.
- All four planes of a a player's color must be finished in order for that player to win.
- Planes can be stacked on top of others with the same color, but they move as separate entities even when stacked (that means if there are two planes of color red on one tile, rolling a 6 will only move one of the planes on the stack). This rule is a deviation we have made from a traditional rule of Aeroplane Chess.
- If a plane lands on a tile who houses plane(s) of an opposite color, it destroys that/those other plane(s) and sends the enemy plane(s) back to its/their hangar. 
- There are also shortcuts located on the board: short haul shortcuts (which allow the plane, upon finishing moving from its normal dieRoll, to skip four tiles upon landing on these) and long haul shortcuts (which allows the plane, upon finishing moving from its normal dieRoll, to skip a quarter of the board, as indicated by the “ladder rungs of equal signs”).
- Planes can only take shortcuts whose tiles have the same color as them.
- Short haul and long haul shortcuts can stack. This means that if you land on a short haul shortcut that takes you to a long haul shortcut, both shortcuts will activate, and the same applies if you land on a long haul shortcut that takes you to a short haul shortcut.
- However when a plane lands on another a shortcut tile that houses an enemy plane, the shortcut will not activate because the plane is “occupied” destroying the enemy.

Some changes we made to Wikipedia page’s rules!
- no stacked movement rule
- rolling a 6 does NOT give an extra die roll
- an even die roll gets planes out of the hangar (instead of specifically 5s or 6s)
- no killing planes located on end tiles when zipping through a shortcut
- a plane does not need an exact roll to go onto the center tile, it just needs to at least pass over the center tile (for ex, if there are 4 tiles between a plane and its center tile, then a die roll of 6 still allows that plane to be marked as "finished" at the end of its movement)
We did not implement any of the "Optional Rules" in the Wikipedia page due to time constraints.

-------------------------------------------------------------------------------

DEVELOPMENT LOG ===============================================================
1/3, Victor: modified the TerminalDemo.java file so that it displays the Aeroplane Chess Board.
1/4, Victor: added a function in TerminalDemo.java so that it can putString from a text file
1/4, Michael: setup the instance variables and constructors for Tile and Plane.
1/6, Victor: added dieRoll function and displayability of the diceRoll
1/6, Michael: added accessors and mutators for Tile
1/7, Michael: modified tile and plane so the movements of the planes are easier
1/7, Victor: added a way to select which plane to take off the hangar (in a primitive, simplified form); EDIT: generalized it so accounts if not all planes are in the hangar
1/8, Michael: got colors to import from a .txt file corresponding to the board
1/8, Victor: added the turn counter, colorized planes. !!need to figure out numPlanesOnLaunchingTiles && fix where planes aren't properly selected after at least one has left the hangar
1/9, Michael: Made tilePath class to orgranize the tiles - similar to circular linked  list
1/9, Victor: made Plane.move() method return an int, for how many planes are on the tile that the plane has moved to... will utilize this to reduce redudant code in updateLaunchTiles() in TerminalClass...
Also fixed updateLaunchTiles()
fixed some bugs
BUG 1:----- discovered new bugs... when there is an even roll & at least one plane out of hangar, there can be a character placed at bottom right corner of the same color of the planeTurn for some reason (62,62)???;
also another bug arises cuz we havent implemented the tile system yet (when we have numPlaneCounterOnTile < 0 and the number counting system shouldnt ever go < 0)
BUG 2:----- also need to implement numberPlane counting system for BOARD tiles (dont need to tell about the color of the planes there bc opposite color planes destroy each other so assumed all tiles with > 1 plane have their planes of the same color)
BUG 3:----- also when there are two planes stacked on top of each other, you can't subtract one from the pile, the whole character has to disappear when moving... can be remedied by not "erasingPlaneLocation" when the tile's planesHere > 1
1/10, Michael: starting to map tiles 
1/10, Victor: figure out createTextTerminal or createTerminal, cuz createTextTerminal works when u ssh into a diff comp, which is gonna be done at the demo, but createTextTerminal also doesnt let me edit @ home on windows... ISSUE WHERE WE REPORT A NULLPOINTEREXCEPTION WILL BE FIXED ONCE WE HAVE A TILE SYSTEM
SOLVED BUG3, but found a new bug where moving a plane doesn't subtract from previous pile?? lol. WAIT THIS IS SOLVED IF A TILE SYSTEM IS IMPLEMENTED
1/11, Victor: fixed a rendering bug of selecting planes and fixed BUG1
1/12, Michael: modified the ascii art for the board and finished mapping tiles
1/12, Victor: we finished the basic movePlane (basic as in didn't implement short haul or long haul shortcuts or endTiles and the visual for numPlanes on tiles and destroying planes)
1/12, Michael: set the proper color for each tile, fixed terminal size issue, fixed small arraylist bug
1/13, Victor: we did a crapton, the commit messages better explain what we did
1/14, Victor: fixed visual glitches when planes killing each other 
1/15, Victor: we can now kill plane stacks of 3+, and made tabbing between planes better
1/16, Michael: started interaction with the end tiles
1/16, Victor: starting main menu
1/17, Victor: fixing bugs relating to endTiles
1/17, Michael: changing the state of the plane after it reaches the end
1/19, Michael: basically finished end tiles, just have to change the issue when selecting a finished plane
1/19, Victor: ^ fixed that issue so user cannot select a fixed plane
1/20, Michael: fixed the text in the start screen and leaderboard screen
1/21, Victor: fixed every bug I have found and updated README


------------------------------------------------------------------------------------------------

BUGS TO FIX: ====================================
NONE, BLESS to God
TO DO: ==========================================

DEV NOTES: =========================================================
so i added dieRollManipulate mode instead so you play through game normally but only change is u can control which die roll you get by pressing Tab. java -cp lanterna.jar;. TerminalClass.java dieRollManipulate
when in dieRollManipulate mode, you can press 'f' to have all the planes finish to test the leaderboard at the end
