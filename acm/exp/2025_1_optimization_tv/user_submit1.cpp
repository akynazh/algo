// 1161708
extern void feature_info(int featurePower[], double featureQuality[], int featureMedian[]);
extern void user_info(int userDistance[]);
extern void screen_control(const int featureValue[]);

#define MAX_FEATURES 20
#define MAX_USERS 800
#define POWER_BUDGET 380
#define MIN_DISTANCE 100
#define MAX_DISTANCE 2500

static int featurePower[MAX_FEATURES];
static double featureQuality[MAX_FEATURES];
static int featureMedian[MAX_FEATURES];
static int prevFeatureValue[MAX_FEATURES];

void process(void)
{
    feature_info(featurePower, featureQuality, featureMedian);
    for (int i = 0; i < MAX_FEATURES; i++)
    {
        prevFeatureValue[i] = 0;
    }
    for (int time = 0; time < 1000; time++)
    {
        int userDistance[MAX_USERS];
        user_info(userDistance);

        int featureValue[MAX_FEATURES];
        int distanceHistogram[25] = {0};
        int userCount[MAX_FEATURES] = {0};

        for (int u = 0; u < MAX_USERS; u++)
        {
            int d = userDistance[u];
            if (d < MIN_DISTANCE)
                d = MIN_DISTANCE;
            if (d > MAX_DISTANCE)
                d = MAX_DISTANCE;

            distanceHistogram[(d - MIN_DISTANCE) / 100]++;
            for (int i = 0; i < MAX_FEATURES; i++)
            {
                int distDiff = (featureMedian[i] - d);
                if (distDiff < 0)
                    distDiff = -distDiff;
                if (distDiff <= 500)
                {
                    userCount[i]++;
                }
            }
        }

        double ratio[MAX_FEATURES];
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            if (featurePower[i] > 0)
            {
                ratio[i] = (featureQuality[i] * userCount[i]) / featurePower[i];
            }
            else
            {
                ratio[i] = 0;
            }
        }

        for (int i = 0; i < MAX_FEATURES; i++)
        {
            featureValue[i] = 0;
        }

        double powerUsed = 0;

        int sortedFeatures[MAX_FEATURES];
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            sortedFeatures[i] = i;
        }

        for (int i = 0; i < MAX_FEATURES - 1; i++)
        {
            for (int j = i + 1; j < MAX_FEATURES; j++)
            {
                if (ratio[sortedFeatures[i]] < ratio[sortedFeatures[j]])
                {
                    int temp = sortedFeatures[i];
                    sortedFeatures[i] = sortedFeatures[j];
                    sortedFeatures[j] = temp;
                }
            }
        }

        for (int idx = 0; idx < MAX_FEATURES; idx++)
        {
            int i = sortedFeatures[idx];

            double currentPower = 0;
            for (int f = 0; f < MAX_FEATURES; f++)
            {
                currentPower += (featurePower[f] * featureValue[f] * featureValue[f]) / 10000.0;
                currentPower += ((prevFeatureValue[f] - featureValue[f]) > 0 ? (prevFeatureValue[f] - featureValue[f]) : (featureValue[f] - prevFeatureValue[f])) * 0.01;
            }

            double powerAvailable = POWER_BUDGET - currentPower;

            if (powerAvailable <= 0)
                break;

            int targetValue;
            if (ratio[i] > 2.0 && userCount[i] > MAX_USERS / 3)
            {
                targetValue = 100;
            }
            else if (ratio[i] > 1.0 && userCount[i] > MAX_USERS / 5)
            {
                targetValue = 80;
            }
            else if (ratio[i] > 0.5 && userCount[i] > MAX_USERS / 10)
            {
                targetValue = 60;
            }
            else if (ratio[i] > 0.2)
            {
                targetValue = 40;
            }
            else
            {
                targetValue = 20;
            }

            if (targetValue > 100)
                targetValue = 100;

            double basePower = (featurePower[i] * targetValue * targetValue) / 10000.0;
            double changePower = ((targetValue - prevFeatureValue[i]) > 0 ? (targetValue - prevFeatureValue[i]) : (prevFeatureValue[i] - targetValue)) * 0.01;
            double totalPowerNeeded = basePower + changePower;

            if (totalPowerNeeded > powerAvailable)
            {
                int low = 0, high = targetValue;
                while (low < high)
                {
                    int mid = (low + high + 1) / 2;
                    double p = (featurePower[i] * mid * mid) / 10000.0;
                    double c = ((mid - prevFeatureValue[i]) > 0 ? (mid - prevFeatureValue[i]) : (prevFeatureValue[i] - mid)) * 0.01;
                    if (p + c <= powerAvailable)
                    {
                        low = mid;
                    }
                    else
                    {
                        high = mid - 1;
                    }
                }
                targetValue = low;
            }

            featureValue[i] = targetValue;
        }

        powerUsed = 0;
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            powerUsed += (featurePower[i] * featureValue[i] * featureValue[i]) / 10000.0;
            powerUsed += ((prevFeatureValue[i] - featureValue[i]) > 0 ? (prevFeatureValue[i] - featureValue[i]) : (featureValue[i] - prevFeatureValue[i])) * 0.01;
        }

        while (powerUsed < POWER_BUDGET - 1.0)
        {
            int improved = 0;

            for (int idx = 0; idx < MAX_FEATURES; idx++)
            {
                int i = sortedFeatures[idx];

                if (featureValue[i] >= 100)
                    continue;
                int newValue = featureValue[i] + 1;
                double oldBasePower = (featurePower[i] * featureValue[i] * featureValue[i]) / 10000.0;
                double newBasePower = (featurePower[i] * newValue * newValue) / 10000.0;
                double oldChangePower = ((prevFeatureValue[i] - featureValue[i]) > 0 ? (prevFeatureValue[i] - featureValue[i]) : (featureValue[i] - prevFeatureValue[i])) * 0.01;
                double newChangePower = ((prevFeatureValue[i] - newValue) > 0 ? (prevFeatureValue[i] - newValue) : (newValue - prevFeatureValue[i])) * 0.01;

                double extraPower = (newBasePower - oldBasePower) + (newChangePower - oldChangePower);

                if (powerUsed + extraPower <= POWER_BUDGET)
                {
                    featureValue[i]++;
                    powerUsed += extraPower;
                    improved = 1;
                    break;
                }
            }

            if (!improved)
                break;
        }
        screen_control(featureValue);
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            prevFeatureValue[i] = featureValue[i];
        }
    }
}