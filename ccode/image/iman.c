/*
 * iman.c
 *
 *  Created on: Apr 9, 2015
 *      Author: Melinda Robertson
 *      Version: Apr 17, 2015
 *
 *      Combines two bitmap images in two ways:
 *          1. Blends colors by averaging rgb values.
 *          2. Creates a checkerboard image that alternates between the two original images.
 */

#include <stdio.h>

//Used for the checker-board image.
#define true 1
#define false 0
typedef int bool;

/**
 * Gets a value from the indicated header.
 */
void getThingy(unsigned char header[][54], int offset, int *thingy) {
    *thingy = *(int*)((char*)header[0] + offset);
}

/**
 * Retrieves a 2D array of the indicated length of data from a file.
 */
void buff(FILE *infile, int length, unsigned char buffer[][length]) {
    fread(buffer, sizeof(char), length, infile);
}

/**
 * Blends the pixel data from two 2D arrays by averaging each pair of values.
 */
void blend(int rows, int columns, unsigned char buffer[rows][columns],
        unsigned char bufferOne[rows][columns],
        unsigned char bufferTwo[rows][columns]) {
    //Integers for loops.
    int r, c;

    for (r = 0; r < rows; r++) {
        for (c = 0; c < columns; c++) {
            buffer[r][c] = (bufferOne[r][c] + bufferTwo[r][c]) / 2;
        }
    }
}

/**
 * Combines pixel data in a checker-board pattern.
 */
void check(int rows, int columns, unsigned char buffer[rows][columns],
        unsigned char bufferOne[rows][columns],
        unsigned char bufferTwo[rows][columns]) {
    //Integers for loops.
    int r, c, count;
    bool turn = true; //switches between buffers
    int checkrow = rows / 8;
    int checkcol = columns / 8;
    for (r = 0; r < rows; r++) {
        if (r % checkrow == 0) {
            if (turn == true)
                turn = false;
            else
                turn = true;
        }
        for (c = 0; c < columns; c += checkcol) {
            if (turn) {
                for (count = c; count < c + checkcol; count++) {
                    buffer[r][count] = bufferOne[r][count];
                }
                turn = false;
            } else {
                for (count = c; count < c + checkcol; count++) {
                    buffer[r][count] = bufferTwo[r][count];
                }
                turn = true;
            }
        }
    }
}

/**
 *
 */
int main() {
    setvbuf(stdout, NULL, _IONBF, 0);
    //Keeps track of the rows, columns and size.
    int columns, rows, size;

    unsigned char headerOne[1][54];
    unsigned char headerTwo[1][54];

    FILE *infileOne, *infileTwo, *outfileOne, *outfileTwo;

    infileOne = fopen("in1.bmp", "rb");
    infileTwo = fopen("in2.bmp", "rb");

    buff(infileOne, 54, headerOne);
    buff(infileTwo, 54, headerTwo);

    //Gets the dimensions
    getThingy(headerOne, 18, &columns);
    getThingy(headerOne, 22, &rows);
    getThingy(headerOne, 34, &size);
    columns = columns * 3;

    //Retrieves pixel data.
    unsigned char bufferOne[rows][columns];
    unsigned char bufferTwo[rows][columns];
    buff(infileOne, size, bufferOne);
    buff(infileTwo, size, bufferTwo);

    fclose(infileOne);
    fclose(infileTwo);

    //Blends pictures
    outfileOne = fopen("blend.bmp", "wr");
    unsigned char bufferblend[rows][columns];
    blend(rows, columns, bufferblend, bufferOne, bufferTwo);
    fwrite(headerOne, sizeof(char), 54, outfileOne);
    fwrite(bufferblend, sizeof(char), size, outfileOne);
    fclose(outfileOne);

    //Checker-boards pictures
    outfileTwo = fopen("checker.bmp", "wr");
    unsigned char buffercheck[rows][columns];
    check(rows, columns, buffercheck, bufferOne, bufferTwo);
    fwrite(headerTwo, sizeof(char), 54, outfileTwo);
    fwrite(buffercheck, sizeof(char), size, outfileTwo);
    fclose(outfileTwo);

    return 0;
}
