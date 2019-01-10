# MKS21X-FinalProject

DEVELOPMENT LOG
1/3, Victor: modified the TerminalDemo.java file so that it displays the Aeroplane Chess Board.
1/4, Victor: added a function in TerminalDemo.java so that it can putString from a text file
1/4, Michael: setup the instance variables and constructors for Tile and Plane.
1/6, Victor: added dieRoll function and displayability of the diceRoll
1/6, Michael: added accessors and mutators for Tile
1/7, Michael: modified tile and plane so the movements of the planes are easier
1/7, Victor: added a way to select which plane to take off the hangar (in a primitive, simplified form); EDIT: generalized it so accounts if not all planes are in the hangar
1/8, Michael: got colors to import from a .txt file corresponding to the board
1/8, Victor: added the turn counter, colorized planes. !!need to figure out numPlanesOnLaunchingTiles && fix where planes aren't properly selected after at least one has left the hangar

1/9, Victor: made Plane.move() method return an int, for how many planes are on the tile that the plane has moved to... will utilize this to reduce redudant code in updateLaunchTiles() in TerminalClass...
Also fixed updateLaunchTiles()
fixed some bugs
discovered new bugs... when there is an even roll & at least two planes out of hangar, there can be a character placed at bottom right corner of the same color of the planeTurn for some reason;
also another bug arises cuz we havent implemented the tile system yet (when we have numPlaneCounterOnTile < 0 and the number counting system shouldnt ever go < 0)
also need to implement numberPlane counting system for BOARD tiles (dont need to tell about the color of the planes there bc opposite color planes destroy each other so assumed all tiles with > 1 plane have their planes of the same color)
also when there are two planes stacked on top of each other, you can't subtract one from the pile, the whole character has to disappear when moving... can be remedied by not "erasingPlaneLocation" when the tile's planesHere > 1