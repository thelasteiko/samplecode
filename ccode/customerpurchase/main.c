/*
 * cp1.c
 *
 *  Created on: Apr 27, 2015
 *      Author: Melinda Robertson
 *      Version: 1 May 2015
 *
 *      Read from input file
 *      Store information in array of structures
 *      Print two files:
 *          chronological listing of customers and items
 *          in order of purchase value
 */

#include <stdio.h>
#include <string.h>

#define STR_LEN 30  //length of strings
#define NUM_CUST 20 //max number of customers
#define NUM_ITEMS 10    //max number of items

struct item { //an item that a customer purchased
    char name[STR_LEN];
    int amount;
    double price;
};

struct customer { //customer with a list of purchased items
    char name[STR_LEN];
    struct item items[NUM_ITEMS];
    int size;
};

/**
 * Looks for a name in the current list of customers; returns the index
 * if found or -1 if not.
 */
int find(struct customer *param, int size, char *name) {
    int i;
    char c;
    for (i = 0; i <= size; ++i) {
        if (!strcmp(param[i].name, name)) {
            return i;
        }
    }
    return -1;
}

/**
 * Adds the indicated customer and item to the list. If the customer is already in the list
 * it adds just the item.
 */
int add(struct customer *param, int size, char *name, char *item_name,
        int amount, double price) {
    int i = find(param, size, name);
    struct item n;
    strcpy(n.name, item_name);
    n.amount = amount;
    n.price = price;
    if (i >= 0) {
        param[i].items[(param[i].size)++] = n;
    } else {
        struct customer c;
        strcpy(c.name, name);
        c.size = 0;
        c.items[c.size++] = n;
        param[size++] = c;
    }
    return size;
}

/**
 * Reads a file with customer data formatted:
 *      CUSTOMER AMOUNT ITEM PRICE
 */
int readFile(FILE *inputfile, struct customer *param, int size) {
    char customer_name[STR_LEN];
    int number_of; //number of whatever item was purchased
    char item_name[STR_LEN];
    double price_of; //price of each item

    int i = 2; //counter to print dots to the console
    printf("Reading File");
    while (fscanf(inputfile, "%s %d %s $%lf", customer_name, &number_of,
            item_name, &price_of) > 0) {

        if (!i) { //this has no academic value
            printf(" . ");
            i = 2;
        }

        size = add(param, size, customer_name, item_name, number_of, price_of);
        i--;
        if (feof(inputfile)) { //stops at the end of the file
            printf("End of File\n");
            break;
        }
    }
    return size;
}

/**
 * Sorts items according the total price of each purchase.
 */
void sortItems(struct item *param, int size) {
    int i, j;
    for (i = 0; i < size - 1; i++) {
        for (j = 0; j < size - i - 1; j++) {
            if ((param[j].price * (double) param[j].amount)
                    < (param[j + 1].price * (double) param[j + 1].amount)) {
                struct item temp = param[j];
                param[j] = param[j + 1];
                param[j + 1] = temp;
            }
        }
    }
}

/**
 * Sums up the purchases for a customer.
 */
double total_purchase(struct item *param, int size) {
    double sum = 0;
    int i;
    for (i = 0; i < size; ++i) {
        sum += (param[i].price * (double) param[i].amount);
    }
    return sum;
}

/**
 * Sorts customers according to the price of the total amount purchased.
 */
void sortPurchaseValue(struct customer *param, int size) {
    int i, j;
    double sum1, sum2;
    for (i = 0; i < size - 1; i++) {
        sortItems(param[i].items, param[i].size);
        for (j = 0; j < size - i - 1; j++) {
            sum1 = total_purchase(param[j].items, param[j].size);
            sum2 = total_purchase(param[j + 1].items, param[j + 1].size);
            if (sum1 < sum2) {
                struct customer temp = param[j];
                param[j] = param[j + 1];
                param[j + 1] = temp;
            }
        }
    }
}

/**
 * Prints to file a summary of customer purchases by order of
 * appearance.
 */
void outTime(FILE *outfile, struct customer *param, int size) {
    int i = 0, j;
    while (i < size) {
        fprintf(outfile, "%s\r\n", param[i].name);
        for (j = 0; j < param[i].size; ++j) {
            fprintf(outfile, "%s %d $%3.2lf\r\n", param[i].items[j].name,
                    param[i].items[j].amount, param[i].items[j].price);
        }
        fprintf(outfile, "\r\n");
        ++i;
    }
}

/**
 * Prints to file a summary of customer purchases by order of
 * value of purchase.
 */
void outMoney(FILE *outfile, struct customer *param, int size) {
    int i = 0, j;
    while (i < size) {
        fprintf(outfile, "%s, Total Order = $%3.2f\r\n", param[i].name,
                total_purchase(param[i].items, param[i].size));
        for (j = 0; j < param[i].size; ++j) {
            fprintf(outfile, "%s %d $%3.2f, Item Value = $%3.2f\r\n",
                    param[i].items[j].name, param[i].items[j].amount,
                    param[i].items[j].price,
                    (param[i].items[j].amount * param[i].items[j].price));
        }
        fprintf(outfile, "\r\n");
        ++i;
    }
}

int main(void) {
    struct customer param[NUM_CUST]; //list of customers
    int size = 0; //number of customers
    FILE *infile, *outfile;

    //Read the input
    infile = fopen("hw4input.txt", "rb");
    size = readFile(infile, param, size);
    fclose(infile);

    //Print in order of appearance.
    outfile = fopen("hw4time.txt", "w");
    outTime(outfile, param, size);
    fclose(outfile);

    //Print in order of purchase value.
    sortPurchaseValue(param, size);
    outfile = fopen("hw4money.txt", "w");
    outMoney(outfile, param, size);
    fclose(outfile);

    return 1;
}
