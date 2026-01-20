#ifndef _CRT_SECURE_NO_WARNINGS
#define _CRT_SECURE_NO_WARNINGS
#endif

#include <stdio.h>

static unsigned long long seed = 5;

static int pseudo_rand(void)
{
	seed = seed * 25214903917ULL + 11ULL;
	return (seed >> 16) & 0x3fffffff;
}

/* These constant variables will NOT be changed */
#define MAP_SIZE 100
#define RESTAURANT_NUM 10
#define DELIVERY_NUM 2000
#define TIME_LIMIT 100000

typedef struct {
	int y, x;
}Coordinates;

typedef struct {
	Coordinates src;
	Coordinates dest;
} Delivery;

static Coordinates gRider;
static Coordinates gRestaurant[RESTAURANT_NUM];
static Delivery gDeliveries[DELIVERY_NUM];
static Delivery gDeliveriesBak[DELIVERY_NUM];
static bool gIsDone[DELIVERY_NUM];
static int gDupChk[MAP_SIZE][MAP_SIZE];
static int gTime;
static long long gTotalScore;
//////////////////////////////////////////

/////////////////////////////////////////
#define ABS(x) ((x) >= 0 ? (x) : -(x))

bool deliver(int mID)
{
	if (mID < 0 || mID >= DELIVERY_NUM || gTime >= TIME_LIMIT || gIsDone[mID] == true)
		return false;

	gTime += ABS(gRider.y - gDeliveries[mID].src.y) + ABS(gRider.x - gDeliveries[mID].src.x);

	gRider.y = gDeliveries[mID].src.y;
	gRider.x = gDeliveries[mID].src.x;

	gTime += ABS(gRider.y - gDeliveries[mID].dest.y) + ABS(gRider.x - gDeliveries[mID].dest.x);

	if (gTime <= TIME_LIMIT) {
		int dist = ABS(gDeliveries[mID].dest.y - gDeliveries[mID].src.y) + ABS(gDeliveries[mID].dest.x - gDeliveries[mID].src.x);
		gTotalScore += 3000 + 300 * dist;
	}
	gRider.y = gDeliveries[mID].dest.y;
	gRider.x = gDeliveries[mID].dest.x;

	gIsDone[mID] = true;

	return true;
}

static void init()
{
	gTime = 0;

	gRider.y = pseudo_rand() % MAP_SIZE;
	gRider.x = pseudo_rand() % MAP_SIZE;

	for (int y = 0; y < MAP_SIZE; y++) {
		for (int x = 0; x < MAP_SIZE; x++) {
			gDupChk[y][x] = 0;
		}
	}

	for (int i = 0; i < RESTAURANT_NUM; i++) {
		int y, x;
		do {
			y = pseudo_rand() % MAP_SIZE;
			x = pseudo_rand() % MAP_SIZE;
		} while (gDupChk[y][x] == 1);

		gDupChk[y][x] = 1;
		gRestaurant[i].y = y;
		gRestaurant[i].x = x;
	}

	for (int i = 0; i < DELIVERY_NUM; i++) {
		int id = pseudo_rand() % RESTAURANT_NUM;

		int y, x;
		do {
			y = pseudo_rand() % MAP_SIZE;
			x = pseudo_rand() % MAP_SIZE;
		} while (gDupChk[y][x] == 1);

		gDeliveries[i].src.y = gRestaurant[id].y;
		gDeliveries[i].src.x = gRestaurant[id].x;
		gDeliveries[i].dest.y = y;
		gDeliveries[i].dest.x = x;
		gIsDone[i] = false;
		gDeliveriesBak[i] = gDeliveries[i];
	}
}

extern void process(Coordinates, Delivery[]);

int main(void)
{
	//	freopen("sample_input.txt", "r", stdin);

	int TC;
	scanf("%d", &TC);

	for (int tc = 0; tc < TC; tc++) {
		scanf("%llu", &seed);

		init();
		process(gRider, gDeliveriesBak);
	}

	long long SCORE = gTotalScore;
	printf("SCORE: %lld\n", SCORE);
	return 0;
}