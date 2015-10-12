/*
 * my_heap.c
 *
 *  Created on: May 25, 2015
 *      Author: Melinda Robertson
 *     Version: May 29 2015
 *
 *     Memory heap for allocation using a linked list to store starting
 *     addresses. Nodes represent the state of each block of memory so that
 *     sequential nodes should alternate between true and false. The sum of
 *     all sizes should equal the total amount of bytes in the pool.
 */

#include <stdio.h>
#include <stdlib.h>
#include "mallok.h"

typedef int bool;   //For determining if block is free
#define TRUE 1
#define FALSE 0

/* Head node representing a block of memory. */
struct heap_node {
    void *start_address;    //The address in the pool
    int size;   //size of the block
    bool free;  //if the block has been allocated
    struct heap_node *next; //the next block
    struct heap_node *prev; //the previous block
};
typedef struct heap_node Block;

static Block *head; //first node in the list
static Block *tail; //last node in the list
static int total; //total amount of bytes in the pool of memory

/* Creates an initial pool of memory. */
void create_pool(int size) {
    head = malloc(sizeof(Block));
    if (!head) {
        printf("Couldn't allocate list.");
        exit(0);
    }
    total = size;
    head->free = TRUE;
    head->size = size;
    head->start_address = malloc(size);

    if (!head->start_address) {
        printf("Couldn't allocate pool.");
        exit(0);
    }

    head->next = NULL;
    head->prev = NULL;
    tail = head;
}

/* Retrieves the starting address of a free block of memory. */
void *my_malloc(int size) {
    if (!head) {
        printf("Heap was not allocated.");
        return NULL;
    }
    if (total < size) { //request too large for pool
        return NULL;
    }
    if (head == tail) { //before anything has been allocated
        head = malloc(sizeof(Block));
        head->size = size;
        head->start_address = tail->start_address;
        head->free = FALSE;
        head->next = tail;
        head->prev = NULL;

        tail->prev = head;
        tail->start_address += size;
        tail->free = TRUE;
        tail->size -= size;
        return head->start_address;
    }
    Block *temp = head;
    while (temp) {  //iterate through to find free block
        if (temp->free) {
            //if the size is equal to the free block, return the block
            if (temp->size == size) {
                temp->free = FALSE;
                return temp->start_address;
            } else if (temp->size > size) {
                //Creating node
                Block *newblock = malloc(sizeof(Block));
                newblock->start_address = temp->start_address;
                newblock->size = size;
                newblock->free = FALSE;
                //Adjusting references
                newblock->next = temp;
                newblock->prev = temp->prev;
                temp->prev = newblock;
                newblock->prev->next = newblock;
                //Updating current node
                temp->start_address += size;
                temp->size -= size;
                return newblock->start_address;
            }
        }
        temp = temp->next;
    }
    return NULL;    //couldn't find an empty node of appropriate size
}

/* Deallocates a list of nodes starting at beg->next and going until end. */
int freeup(Block *beg, Block *end) {
    int size = 0;
    while (beg != end) {
        size += end->size;
        end = end->prev;
        free(end->next);
    }
    return size;
}

/* Finds and combines nodes that are free. */
void combine() {
    Block *temp, *beg, *end;
    temp = beg = end = head;
    int size = 0;
    while (temp) {
        if (temp->free) {   //start beginning of free nodes
            beg = end = temp;
            while (end->next && end->free) {
                end = end->next;
            }   //find end; end->free is FALSE or end == tail
            if (beg != end) {
                if (!end->next) { //end is tail node
                    if (tail->free) { //tail is free
                        size = freeup(beg, tail);
                        tail = beg;
                        tail->next = NULL;
                    } else { //tail is not free
                        size = freeup(beg, tail->prev);
                        beg->next = tail;
                        tail->prev = beg;
                    }
                } else {
                    //free up interim nodes
                    size = freeup(beg, end->prev);
                    //combine beg and end
                    beg->next = end;
                    end->prev = beg;
                }
                beg->size += size;
            }
        }
        temp = temp->next;
    }
}

/* Returns a block of memory to the pool. */
void my_free(void *block) {
    if (!head) {
        printf("Heap was not allocated.");
        return;
    }
    Block *temp = head;
    while (temp) {
        if (temp->start_address == block) {
            temp->free = TRUE;  //sets block to free
            combine();  //finds and combines free blocks
            return;
        }
        temp = temp->next;
    }
}

/* Destroys all nodes, free or taken, and deallocates the memory pool. */
void destroy_pool() {
    if (!head) {
        printf("Pool was never created.");
        return;
    }
    while (head != tail) {
        tail = tail->prev;
        free(tail->next);
    }
    free(head->start_address);
    free(head);
    total = 0;
}

