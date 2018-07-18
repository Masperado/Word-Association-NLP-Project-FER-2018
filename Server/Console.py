#!/bin/python

"""console applikacija"""
from CroWord2VecModel import CroWord2VecModel
from EngWord2VecModel import EngWord2VecModel

model_ana = ""
model_gene = ""


def main():
    case = input("R za rješavač, G za generator: ").upper()
    if case == "R":
        solve_for_associations()
    elif case == "G":
        generate_association()
    else:
        print("Nevaljan unos!")


def solve_for_associations():
    associations = [[], [], [], []]
    column_solutions = []
    skip_list = []
    for i in range(0, 4):
        for j in range(0, 4):
            user_input = input("Unesi asocijaciju " + str(i + 1) + str(j + 1) + ": ")
            while len(user_input) < 3:
                print("Unesite pojam duži od 2 slova!")
                user_input = input("Unesi asocijaciju " + str(i + 1) + str(j + 1) + ": ")
            associations[i].append(user_input)
            skip_list.append(user_input)
    for i in range(0, 4):
        column_solutions.append(model_ana.generate_solution(associations[i], skip_list))
        skip_list.append(column_solutions[i])
    solution = model_ana.generate_solution(column_solutions, skip_list)
    print_association(associations, column_solutions, solution)


def generate_association():
    lista_imena = ["luka", "ivan", "josip", "ivana", "milan", "ivica", "ant", "marko", "tomislav", "damir", "marija",
                   "jadranka", "goran", "vladimir",
                   "zoran", "nikola", "robert", "željko", "stjepan", "david", "boris", "igor", "martin", "petar",
                   "mladen", "marina"];

    lista_drzava = ["srbija", "hrvatska", "slovenija", "njemacka", "italija", "kina", "francuska", "rusija", "bosna", "gora",
                    "crna", "dalmacija", "amerika", "britanija", "austrija", "španjolska", "hercegovina", "jugoslavija", "kosovo", "velika", "irak"]

    solution = model_gene.generate_random_word()  # zadnja rijeci za pogodit

    while (solution in lista_imena or solution in lista_drzava):
        solution = model_gene.generate_random_word()

    column_solutions = model_gene.generate_word_list(solution, [])  # prve 4 rijeci za pogodit

    # prve 4 rijeci za pogodit
    associations = [[], [], [], []]
    skip_list = [solution, *column_solutions]
    for i in range(0, 4):
        associations[i] = model_gene.generate_word_list(column_solutions[i], skip_list)
        for j in range(0, len(associations[i])):
            skip_list.append(associations[i][j])
    print_association(associations, column_solutions, solution)


def print_association(associations, column_solutions, solution):
    print("\n")
    for i in range(0, 4):
        print("", end=" | ")
        for j in range(0, 4):
            print("{:20s}".format(associations[j][i]), end=" | ")
        print("\n")
    print("----------------------------------------------------------------------------------------------")
    print("", end=" | ")
    for k in range(0, 4):
        print("{:20s}".format(column_solutions[k]), end=" | ")
    print("\n----------------------------------------------------------------------------------------------")
    print("------------------------------------------- " + solution + " -------------------------------------------")


if __name__ == "__main__":
    language = input("H za hrvatski, E za engleski: ").upper()
    if language == "H":
        model_gene = CroWord2VecModel('hrv_vector_dict_gene.bin')
        model_ana = CroWord2VecModel('hrv_vector_dict_ana.bin')
    elif language == "E":
        model_gene = EngWord2VecModel('/home/vinko/Storage/datasets/GoogleNews-vectors-negative300-SLIM-filtered.bin')
        model_ana = model_gene
        #model_ana = EngWord2VecModel('GoogleNews.bin')
    else:
        print("Nevaljan unos!")
        exit(1)
    choice = "DA"
    while choice == "DA":
        main()
        print()
        choice = input("Želite li nastaviti(DA/NE): ").upper()
        print()
