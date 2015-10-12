/*
 * mallok_test.c
 *
 *  Created on: May 29, 2015
 *      Author: Melinda Robertson
 *     Version: May 29 2015
 *
 *     Test program for memory allocation heap.
 */

#include <stdio.h>
#include "mallok.h"

/*
 * Request a pool of 1000. Count how many times you can request a block of size 10.
 */
int test_one() {
    printf("Test One: Blocks of size 10\n");
    create_pool(1000);
    int i = 0;
    void *bl = my_malloc(10);
    while (bl) {
        bl = my_malloc(10);
        i++;
    }
    printf("Blocks Created: %d\n", i);
    destroy_pool();
    return 1;
}

/*
 * Create a pool of 1000 bytes.
 * Make 5 requests for blocks of 200 bytes.
 * Free all 5 blocks.
 * Repeat this request/free pattern several times to make sure you can reuse blocks after they are returned.
 */
int test_two() {
    printf("Test Two: Blocks of 200 bytes\n");
    create_pool(1000);
    int i = 5;
    void *bl1, *bl2, *bl3, *bl4, *bl5;
    while (i) {
        printf("Round: %d...", i);
        bl1 = my_malloc(200);
        bl2 = my_malloc(200);
        bl3 = my_malloc(200);
        bl4 = my_malloc(200);
        bl5 = my_malloc(200);

        my_free(bl1);
        my_free(bl2);
        my_free(bl3);
        my_free(bl4);
        my_free(bl5);
        i--;
        printf("Success.\n");
    }
    destroy_pool();
    return 1;
}

/*
 * Create a pool of 1000 bytes.
 * Make 5 requests for blocks of 200 bytes.
 * Free the middle block.
 * Request a block of 210 bytes (it should fail)
 * Request a block of 150 bytes (it should succeed)
 * Request a block of 60 bytes (it should fail)
 * Request a block of 50 bytes (it should succeed) etc.
 */
int test_three() {
    printf("Test Three: Free Middle Block\n");
    int error = 1;
    create_pool(1000);
    void *bl1 = my_malloc(200);
    void *bl2 = my_malloc(200);
    void *bl3 = my_malloc(200);
    void *bl4 = my_malloc(200);
    void *bl5 = my_malloc(200);

    my_free(bl3);
    bl3 = my_malloc(210);
    if (!bl3) {
        printf("Memory not allocated: %d\n", 210);
    } else {
        printf("Memory allocated: %d\n", 210);
        error = 2;
    }
    bl3 = my_malloc(150);
    if (!bl3) {
        printf("Memory not allocated: %d\n", 150);
        error = 3;
    } else {
        printf("Memory allocated: %d\n", 150);
    }
    void *bl6 = my_malloc(60);
    if (!bl6) {
        printf("Memory not allocated: %d\n", 60);
    } else {
        printf("Memory allocated: %d\n", 60);
        error = 4;
    }
    bl6 = my_malloc(50);
    if (!bl6) {
        printf("Memory not allocated: %d\n", 50);
        error = 5;
    } else {
        printf("Memory allocated: %d\n", 50);
    }
    my_free(bl1);
    my_free(bl2);
    my_free(bl3);
    my_free(bl4);
    my_free(bl5);
    my_free(bl6);
    printf("Test Three Complete.\n");
    destroy_pool();
    return error;
}

/*
 * Create a pool of 1000 bytes.
 * Request 5 blocks of 200 bytes.
 * Fill the first block with the letter 'A', the second block with 'B', etc.
 * Verify that the values stored in each block are still there.
 * (Is the first block full of A's, second block full of B's, etc.)
 */
int test_four() {
    printf("Test Four: Storing characters\n");
    create_pool(1000);
    char *bl1 = my_malloc(200);
    char *bl2 = my_malloc(200);
    char *bl3 = my_malloc(200);
    char *bl4 = my_malloc(200);
    char *bl5 = my_malloc(200);
    int i;

    for (i = 0; i < 199; i++) {
        bl1[i] = 'A';
        bl2[i] = 'B';
        bl3[i] = 'C';
        bl4[i] = 'D';
        bl5[i] = 'E';
    }
    bl1[199] = '\0';
    bl2[199] = '\0';
    bl3[199] = '\0';
    bl4[199] = '\0';
    bl5[199] = '\0';
    printf("%s\n", bl1);
    printf("%s\n", bl2);
    printf("%s\n", bl3);
    printf("%s\n", bl4);
    printf("%s\n", bl5);

    printf("Test Four Complete.\n");
    destroy_pool();
    return 1;
}

/*
 * Create a pool of 1000 bytes.
 * Request and return a block of 1000 bytes
 * Request and return four blocks of 250 bytes
 * Request and return ten blocks of 100 bytes
 */
int test_five() {
    printf("Test Five: Request and Return\n");
    create_pool(1000);

    printf("Request 1000...");
    void *bl1 = my_malloc(1000);
    my_free(bl1);
    printf("Complete.\n");

    printf("Request 250*4...");
    bl1 = my_malloc(250);
    void *bl2 = my_malloc(250);
    void *bl3 = my_malloc(250);
    void *bl4 = my_malloc(250);
    my_free(bl1);
    my_free(bl2);
    my_free(bl3);
    my_free(bl4);
    printf("Complete.\n");

    printf("Request 100*10...");
    bl1 = my_malloc(100);
    bl2 = my_malloc(100);
    bl3 = my_malloc(100);
    bl4 = my_malloc(100);
    void *bl5 = my_malloc(100);
    void *bl6 = my_malloc(100);
    void *bl7 = my_malloc(100);
    void *bl8 = my_malloc(100);
    void *bl9 = my_malloc(100);
    void *bl10 = my_malloc(100);
    my_free(bl1);
    my_free(bl2);
    my_free(bl3);
    my_free(bl4);
    my_free(bl5);
    my_free(bl6);
    my_free(bl7);
    my_free(bl8);
    my_free(bl9);
    my_free(bl10);
    printf("Complete.\n");

    printf("Test Five Complete.\n");
    destroy_pool();
    return 1;
}

int main(void) {
    printf("Start\n");
    int n = test_one();
    printf("Test One: %d\n", n);
    n = test_two();
    printf("Test Two: %d\n", n);
    n = test_three();
    printf("Test Three: %d\n", n);
    n = test_four();
    printf("Test Four: %d\n", n);
    n = test_five();
    printf("Test Five: %d\n", n);
    return 1;
}

