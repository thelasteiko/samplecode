/*
 * mallok.h
 *
 *  Created on: May 25, 2015
 *      Author: Melinda Robertson
 *     Version: May 29 2015
 *
 *     Header file for memory allocation heap.
 */

#ifndef MALLOK_H_
#define MALLOK_H_

void create_pool(int size);
void *my_malloc(int size);
void my_free(void *block);
void destroy_pool();    //added for convenience

#endif /* MALLOK_H_ */
