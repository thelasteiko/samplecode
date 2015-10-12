/*
 * list.h
 *
 *  Created on: May 14, 2015
 *      Author: Melinda Robertson
 *      Version: May 22 2015
 *
 *      Contract header for a linked list. Incorporates basic list operations.
 */

#ifndef LIST_H_
#define LIST_H_

#define WORD_COUNT 30   //the max number of characters for each word
#define TOP 50      //the number of words to write to file
typedef struct node_type *Node; //nodes for a linked list

Node create(void);
Node find(Node list, char* str);
Node add(Node list, char* str, int whichfile);
int get_dif(Node n);
void swap(Node* n1, Node* n2);
Node sort(Node head);
void print_csv(FILE* file, Node head);

#endif /* LIST_H_ */
