帮我完成 user.cpp，分数要尽可能高，要高于 1200000

---

g++ -O3 -o main main.cpp user.cpp && echo "编译成功" && ./main < test_input.txt

---

Execution Time
1, 000 ms per test case

There is a large TV at the airport, equipped with sensors that can measure the distance between the TV and its viewers.
Each feature of the TV must be adjusted in real time to maximize the total sum of customer satisfaction.

[Cautions]
1. No header file can be added to the User Code.
(The only exception is malloc.h.)
2. The Main is used as is when the test is graded.
(Defensive code can be added to prevent cheating.)
3. This Certificate Test is subject to an extremely strict code review.
Direct access to the variables used in the Main is not allowed.
4. This test supports C++ only.
5. The maximum number of test cases for grading is 100.

▶ Maximize SCORE by writing the process() function.

For more details, analyze the given code.
Sample Input : 
10
1
2
3
4
5
6
7
8
9
10
Main(main.cpp)
#ifndef _CRT_SECURE_NO_WARNINGS
#define _CRT_SECURE_NO_WARNINGS
#endif

#include <stdio.h>

#define MAX_FEATURES    20
#define MAX_USERS      800
#define TIME_STEPS    1000
#define MIN_DISTANCE   100
#define MAX_DISTANCE  2500
#define POWER_BUDGET   380

#define ABS(x) ((x) < 0 ? -(x) : (x))

	static unsigned long long gSeed = 5;

static int pseudo_rand(void)
{
	gSeed = gSeed * 25214903917ULL + 11ULL;
	return (int)((gSeed >> 16) & 0x3fffffff);
}

static int gFeaturePower[MAX_FEATURES];
static double gFeatureQuality[MAX_FEATURES];
static int gFeatureMedian[MAX_FEATURES];

static int gCurrentTime;
static int gCurrentUserDistance[MAX_USERS];
static int gPrevFeatureValue[MAX_FEATURES];
static int spotDistance;

static double gTotalScore;

static double user_satisfaction(const int* userDistance, const int* featureValue);

////////////////////////////////// APIs ////////////////////////////////////////////

void feature_info(int featurePower[], double featureQuality[], int featureMedian[])
{
	for (int i = 0; i < MAX_FEATURES; i++) {
		featurePower[i] = gFeaturePower[i];
		featureQuality[i] = gFeatureQuality[i];
		featureMedian[i] = gFeatureMedian[i];
	}
}

void user_info(int userDistance[])
{
	for (int u = 0; u < MAX_USERS; u++)
		userDistance[u] = gCurrentUserDistance[u];
}

void screen_control(const int featureValue[])
{
	if (gCurrentTime >= TIME_STEPS) gTotalScore = -1;
	if (gTotalScore < 0) return;

	for (int i = 0; i < MAX_FEATURES; i++) {
		int v = featureValue[i];
		if (v < 0 || v > 100) {
			gTotalScore = -1;
			return;
		}
	}

	double powerUsed = 0.0f;
	for (int i = 0; i < MAX_FEATURES; i++) {
		powerUsed += (gFeaturePower[i] * featureValue[i] * featureValue[i]) / 10000.0;
		powerUsed += ABS(gPrevFeatureValue[i] - featureValue[i]) * 0.01;
		gPrevFeatureValue[i] = featureValue[i];
	}

	if (powerUsed > POWER_BUDGET) {
		gTotalScore = -1;
		return;
	}

	double satisfaction = user_satisfaction(gCurrentUserDistance, featureValue);
	gTotalScore += satisfaction - powerUsed * 0.01;

	if (gCurrentTime % 10 == 0) {
		spotDistance = (pseudo_rand() % (MAX_DISTANCE - MIN_DISTANCE + 1)) + MIN_DISTANCE;
	}

	for (int u = 0; u < MAX_USERS; u++) {
		int noise = pseudo_rand() % 10;
		if (gCurrentUserDistance[u] > spotDistance) {
			gCurrentUserDistance[u] -= noise;
		}
		else {
			gCurrentUserDistance[u] += noise;
		}
	}
	gCurrentTime++;
}

/////////////////////////////// End of APIs //////////////////////////////////////

static double user_satisfaction(const int* userDistance, const int* featureValue)
{
	double sumSatisfaction = 0.0f;

	for (int u = 0; u < MAX_USERS; u++) {
		int d = userDistance[u];
		if (d < MIN_DISTANCE) d = MIN_DISTANCE;
		if (d > MAX_DISTANCE) d = MAX_DISTANCE;

		double qualityFactor = 0;
		for (int i = 0; i < MAX_FEATURES; i++) {
			qualityFactor += ((double)featureValue[i] * gFeatureQuality[i] * 0.01) / ((double)(ABS(gFeatureMedian[i] - d) + 1));
		}
		sumSatisfaction += qualityFactor;
	}

	return sumSatisfaction;
}

static void init(void)
{
	for (int i = 0; i < MAX_FEATURES; i++) {
		gFeaturePower[i] = 10 + (pseudo_rand() % 71);
		gFeatureQuality[i] = 0.10f + (pseudo_rand() % 241) / 100.0f;
		gFeatureMedian[i] = (pseudo_rand() % (MAX_DISTANCE - MIN_DISTANCE + 1)) + MIN_DISTANCE;
		gPrevFeatureValue[i] = 0;
	}

	gCurrentTime = 0;
	gTotalScore = 0.0;

	for (int u = 0; u < MAX_USERS; u++) {
		gCurrentUserDistance[u] = MIN_DISTANCE + (pseudo_rand() % (MAX_DISTANCE - MIN_DISTANCE));
	}
}

static void verify(void)
{
	if (gCurrentTime != TIME_STEPS)
		gTotalScore = -1;
}

extern void process(void);

int main(void)
{
	//    freopen("sample_input.txt", "r", stdin);

	long long SCORE = 0;
	int TC_COUNT;
	scanf("%d", &TC_COUNT);
	for (int tc = 1; tc <= TC_COUNT; ++tc) {
		scanf("%llu", &gSeed);
		init();
		process();
		verify();

		if (gTotalScore < 0) {
			SCORE = 0;
			break;
		}
		SCORE += (long long)gTotalScore;
	}

	printf("SCORE: %lld\n", SCORE);
	return 0;
}

user code

```c
// user.cpp
extern void feature_info(int featurePower[], double featureQuality[], int featureMedian[]);
extern void user_info(int userDistance[]);
extern void screen_control(const int featureValue[]);

void process(void)
{

}
```

输入
For more details, analyze the given code.
输出
For more details, analyze the given code.
样例输入 Copy
10
1
2
3
4
5
6
7
8
9
10
样例输出 Copy
SCORE: 1203123

---

test_input.txt

```txt
10
1
2
3
4
5
6
7
8
9
10
```
