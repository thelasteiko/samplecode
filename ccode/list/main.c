/*
 * main.c
 *
 *  Created on: May 14, 2015
 *      Author: Melinda Robertson
 *      Version: 22 May 2015
 *
 *      Create a linked list of words from two files and save the top
 *      50 according to difference in frequency.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "list.h"

/* Loads and stores the frequencies of the specified file. */
Node load(Node head, char* fname, int whichfile) {
    printf("Adding Words...");
    FILE *file = fopen(fname, "rb");

    char line[60];
    char *str = (char*) calloc((WORD_COUNT + 1), sizeof(char));

    int i = 0, j;

    while (fscanf(file, "%s", line)) { //scans each character
        printf("%s\n", line);

        for (j = 0; j < strlen(line); ++j) {
            if (line[j] >= 'A' && line[j] <= 'Z') //checks for uppercase
                line[j] = (char) tolower(line[j]);
            if ((line[j] >= 'a' && line[j] <= 'z') || line[j] == '-'
                    || line[j] == '\'') { //adds to string if valid
                str[i++] = line[j];
            }
        }

        if (i) { //if there is a valid string, save it
            str[i] = '\0';
            printf("%s\n", str);
            i = 0;
            head = add(head, str, whichfile);
            free(str);
            str = (char*) calloc((WORD_COUNT + 1), sizeof(char));
        }

        if (feof(file)) { //if the end of file is reached, break
            printf("End of File\n");
            break;
        }
    }
    fclose(file);
    return head;
}

/* Saves the top words to a file. */
void save(Node head, char* fname) {
    FILE *file;
    file = fopen(fname, "w");
    fprintf(file, "Word,RedBadge,LittleRegiment\n");

    print_csv(file, head); //currently saves 50 words

    fclose(file);
}

/* Load-Sort-Save */
int main(void) {
    setvbuf(stdout, NULL, _IONBF, 0);

    printf("Creating List...");
    Node head = create();

    head = load(head, "RedBadge.txt", 0);
    head = load(head, "LittleRegiment.txt", 1);

    printf("Sorting...");
    head = sort(head);

    printf("Saving To File...");
    save(head, "stat.csv");
    printf("Done.");
    return 0;
}

