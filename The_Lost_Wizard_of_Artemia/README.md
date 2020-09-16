# The Lost Wizard of Artemia

For a game called "The Lost Wizard of Artemia" you need to create the "teleportation spell" routine.
Inputs:

Cities.csv = a list of cities from Artemia
Characters.csv = the list of characters from the game

The teleportation spell:

- Read the two files and randomly place the characters in one city making sure that there is no more than one character in one city. Write the output to a csv file called "InitialHomesOfArtemia.csv".
- Create a the "teleportation spell" routine: 4 random characters change location to a random different city. If the destination city already has two or more other characters, the teleported characted is send to "TheNowhere" and will not be teleported again during other spells. Print the result to the console.
- After the 42 "spells" are performed, write a csv file containing the path of all the characters. The header of the csv should be the name of the characters.
