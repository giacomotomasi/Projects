import numpy as np
import pandas as pd


cities_df = pd.read_csv('/Users/.../Cities.csv', header=None)
characters_df = pd.read_csv('/Users/.../Characters.csv', header=None)  # set path

cities = cities_df.values  # array of 14, shape (14,1)
cities = np.squeeze(cities)  # shape 14
characters = characters_df.values  # array of 8, shape (8,1)
characters = np.squeeze(characters)  # shape 8

############################## INITIALIZATION ###################################

InitialHomesOfArtemia = np.array([characters, np.zeros(8)])  # copy all characters
random_cities = np.random.choice(cities, size=len(characters), replace=False)  # get a set of random number to be used as index
InitialHomesOfArtemia[1,:] = random_cities  # add to "InitialHomesOfArtemia" the cities corresponding to the random index found before
data = pd.DataFrame(data=InitialHomesOfArtemia)
data.to_csv('/Users/.../InitialHomesOfArtemia.csv', index=False, header=None)  # create csv file (hiding the index) # set path

################################################################################


HomesOfArtemia = InitialHomesOfArtemia # duplicate initial condition to add new data
characters_list = InitialHomesOfArtemia[0, :]  # copy character list and it will contain the list of characters that can be moved
new_HomesOfArtemia = InitialHomesOfArtemia[1, :]  # copy initial cities array and it will contain the new cities updated at each step


def teleportation_spell(n, current_city, list):
    random_characters = np.random.choice(list, size=n, replace=False)
    random_cities = np.random.choice(cities, size=n, replace=False)

    for i in range(random_characters.size):
        idx = np.where(random_characters[i] == list)  # find the character index of the selected character in "list"
        idx2 = np.where(random_characters[i] == InitialHomesOfArtemia)  # find the character index of the selected character in "InitialHomesOfArtemia"
        num_character_city = np.count_nonzero(current_city == random_cities[i])  # checks how many times the city is in the array, i.e, how many characters are in that city

        # idx_TheNowhere = np.where(current_city == 'TheNowhere')
        # current_city[idx_TheNowhere] = ''

        if num_character_city >= 2:
            current_city[idx2[1]] = 'TheNowhere'
            list = np.delete(list, idx)  # when a character is sent to TheNowhere it is removed from next possible eselection
        else:
            current_city[idx2[1]] = random_cities[i]  # if the city contains less than 2 characters, move the character there

        updated_characters_list = list

    return current_city, updated_characters_list


for i in range(42):
    n = 4
    new_HomesOfArtemia, characters_list = teleportation_spell(n, new_HomesOfArtemia, characters_list)

    ## to avoid duplicated when a character ends up in TheNowhere
    if 'TheNowhere' in HomesOfArtemia[i+1, :]:
        idx_TheNowhere = np.where(HomesOfArtemia[i+1, :] == 'TheNowhere')
        new_HomesOfArtemia[idx_TheNowhere] = ''

    HomesOfArtemia = np.vstack((HomesOfArtemia, new_HomesOfArtemia)) # tracking data by adding new rows

    ## in case at least 4 characters end up in "TheNowhere" end the process and print a message
    if characters_list.size < n:
        print("Insufficient number of remaining characters to continue!")
        print("Simulation interrupted after", i + 1, "steps.")
        break


################# PRINT PATH ########################################################

path_data = pd.DataFrame(data=HomesOfArtemia)
path_data.to_csv('/Users/.../characters_path.csv', index=False)  # set path
