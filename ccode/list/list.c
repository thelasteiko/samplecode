/*
 * list.c
 *
 *  Created on: May 14, 2015
 *      Author: Melinda Robertson
 *      Version: 22 May 2015
 *
 *      A linked list of words with frequencies from two different files.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "list.h"

struct node_type { //The nodes in the list.
    char word[WORD_COUNT + 1]; //The word from a file.
    struct node_type* next; //The next word's node.
    int badgecount; //How many of this word are in RedBadge.txt
    int littlecount; //How many of this word are in LittleRegiment.txt
};

/* Initializes an empty list. */
Node create() {
    return NULL;
}

/* Finds the Node with the specified word. If no such Node exists, returns NULL */
Node find(Node head, char *str) {
    Node temp = head;
    int i = 0;
    while (temp) {
        if (!strcmp(temp->word, str)) {
            return temp;
        }
        temp = temp->next;
        i++;
    }
    return NULL;
}

/* Adds a word to the list. If the word is in the list
 * it increments the counter. If not it creates a new Node
 * and inserts it into the front of the list.
 * whichfile = 0 for Red Badge of Courage
 * whichfile = 1 for Little Regiment
 */
Node add(Node head, char *str, int whichfile) {
    Node temp = find(head, str);

    if (temp == NULL) {
        temp = malloc(sizeof(struct node_type));
        strcpy(temp->word, str);
        temp->badgecount = 0;
        temp->littlecount = 0;
        temp->next = head;
        head = temp;
    }

    if (!whichfile)
        temp->badgecount++;
    else
        temp->littlecount++;
    return head;
}

/* Returns the difference in frequencies between two words. */
int get_dif(Node n) {
    int dif = abs((n->badgecount) - (n->littlecount));
    return dif;
}

/* Swaps information contained in two Nodes without swapping the Nodes themselves. */
void swap(Node* n1, Node* n2) {
    int bc = (*n1)->badgecount;
    int lc = (*n1)->littlecount;
    char* str = malloc(sizeof(char) * (WORD_COUNT + 1));
    strcpy(str, (*n1)->word);

    (*n1)->badgecount = (*n2)->badgecount;
    (*n1)->littlecount = (*n2)->littlecount;
    strcpy((*n1)->word, (*n2)->word);

    (*n2)->badgecount = bc;
    (*n2)->littlecount = lc;
    strcpy((*n2)->word, str);
}

/* Bubble Sorts Nodes according to differences in frequency. */
Node sort(Node head) {
    Node n1 = head;
    int count = 0;
    while (n1) {
        n1 = n1->next;
        count++;
    }
    int i;
    for (i = 0; i < count; ++i) {
        n1 = head;
        while (n1->next) {
            if (get_dif(n1) < get_dif(n1->next)) {
                swap(&n1, &n1->next);
            }
            n1 = n1->next;
        }
    }

    return head;
}

/* Print a comma delimited representation of each node to the specified file. */
void print_csv(FILE* file, Node head) {
    int i = 0;
    while (head && i <= TOP) {
        fprintf(file, "%s,%d,%d\n", head->word, head->badgecount,
                head->littlecount);
        head = head->next;
        i++;
    }
}

