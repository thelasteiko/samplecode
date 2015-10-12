/*
 * binfloat.c
 *
 *  Created on: Jun 3, 2015
 *      Author: Mindy
 *     Version: Jun 4, 2015
 *
 *     Reads a float given by the user and breaks it down to its binary parts.
 *     Then it reconstructs the float by using the individual bits.
 */

#include <stdio.h>
#include <math.h>

typedef union { //union representing the float
    unsigned int i;
    float f;
} Numeral;

/* Displays a float as a binary number,
 * separating sign, exponent and fraction. */
void display_bits(Numeral num) {
    unsigned int i, k; //i for iteration, k as a holder to bitshift
    k = num.i;
    printf("Your float in 32 bits: ");
    for (i = 0; i < 32; i++) {
        if (k & 0x80000000) {
            printf("1");
        } else {
            printf("0");
        }
        k = k << 1;
    }
    k = num.i;
    printf("\nSign: ");
    if (k & 0x80000000) {
        printf("1");
    } else {
        printf("0");
    }
    k = k << 1;
    printf("\nExponent: ");
    //separate the exponent
    for (i = 0; i < 8; i++) {
        if (k & 0x80000000) {
            printf("1");
        } else {
            printf("0");
        }
        k = k << 1;
    }

    printf("\nFraction: ");
    for (i = 9; i < 32; i++) {
        if (k & 0x80000000) {
            printf("1");
        } else {
            printf("0");
        }
        k = k << 1;
    }
    printf("\n");
}

/* Builds the fraction portion of a float by adding divisions
 * of 2 according to the binary representation. */
float build_frac(Numeral num) {
    unsigned int k, i;
    float build = 1.0;
    float current = .5;
    printf("\nCreating the fraction:\n");
    printf("fraction = %f (implicit one)\n", build);
    k = num.i << 9;
    for (i = 9; i < 32; i++) {
        if (k & 0x80000000) {
            build += current;
            printf("fraction = %f after adding %f\n", build, current);
        } else {
            printf("fraction = %f after skipping %f\n", build, current);
        }
        current /= 2;
        k = k << 1;
    }
    return build;
}

/* Applies the exponent to the calculated fraction portion of the float. */
float apply_exponent(Numeral num, float build) {
    unsigned int k;
    printf("\nApplying the exponent:\n");
    k = num.i << 1;
    short t = 0;
    int u = 7;
    while (u >= 0) {
        if (k & 0x80000000) {
            t += pow(2, u);
        }
        k = k << 1;
        u = u - 1;
    }

    t = t - 127;
    printf("Unbiased exponent: %d\n", t);
    u = t;
    if (t > 0) {
        while (u > 0) {
            build = build * 2;
            printf("times 2 = %f\n", build);
            u = u - 1;
        }
    } else {
        while (u < 0) {
            build = build / 2;
            printf("divided by 2 = %f\n", build);
            u = u + 1;
        }
    }
    return build;
}

int main(void) {
    setvbuf(stdout, NULL, _IONBF, 0);

    Numeral num; //holds the float as a union of float and int
    //build of the float retrieved from binary representation
    float build = 0;
    printf("Enter a float: ");
    scanf("%f", &num.f);
    printf("Your float was read as: %f\n", num.f);

    display_bits(num); //display bit representation
    build = build_frac(num); //build the fraction portion
    build = apply_exponent(num, build); //apply the unbiased exponent

    if (num.i & 0x80000000) { //check sign
        build = -build;
    }
    printf("\nFinal Answer:\t%f", build);

    return 0;
}
