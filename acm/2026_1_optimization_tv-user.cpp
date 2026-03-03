extern void feature_info(int featurePower[], double featureQuality[], int featureMedian[]);
extern void user_info(int userDistance[]);
extern void screen_control(const int featureValue[]);

#define MAX_FEATURES 20
#define MAX_USERS 800
#define POWER_BUDGET 380
#define MIN_DISTANCE 100
#define MAX_DISTANCE 2500
#define ABS(x) ((x) < 0 ? -(x) : (x))

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
        
        // Count users affected by each feature within distance threshold
        int userCount[MAX_FEATURES] = {0};
        double userWeightedScore[MAX_FEATURES] = {0};
        
        // Dynamic distance threshold based on time progress
        int distThreshold = 460 + (time / 150) * 25;  
        if (distThreshold > 700) distThreshold = 700;

        for (int u = 0; u < MAX_USERS; u++)
        {
            int d = userDistance[u];
            if (d < MIN_DISTANCE) d = MIN_DISTANCE;
            if (d > MAX_DISTANCE) d = MAX_DISTANCE;

            for (int i = 0; i < MAX_FEATURES; i++)
            {
                int distDiff = (featureMedian[i] - d);
                if (distDiff < 0) distDiff = -distDiff;
                
                // Fine-tuned distance weighting function
                double weight = 210.0 / (distDiff + 32);
                userWeightedScore[i] += weight;
                
                if (distDiff <= distThreshold)
                {
                    userCount[i]++;
                }
            }
        }

        // Calculate efficiency ratio for power allocation
        double ratio[MAX_FEATURES];
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            if (featurePower[i] > 0)
            {
                // Optimize weighting - more emphasis on weighted score
                double score = userCount[i] * 0.45 + userWeightedScore[i] * 0.55;
                ratio[i] = (featureQuality[i] * score) / featurePower[i];
            }
            else
            {
                ratio[i] = 0;
            }
        }

        // Sort features by efficiency ratio (bubble sort)
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

        // Initialize all feature values to 0
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            featureValue[i] = 0;
        }

        // Allocate power to features based on efficiency ratio
        for (int idx = 0; idx < MAX_FEATURES; idx++)
        {
            int i = sortedFeatures[idx];

            // Calculate current power usage
            double currentPower = 0;
            for (int f = 0; f < MAX_FEATURES; f++)
            {
                currentPower += (featurePower[f] * featureValue[f] * featureValue[f]) / 10000.0;
                currentPower += ABS(prevFeatureValue[f] - featureValue[f]) * 0.01;
            }

            double powerAvailable = POWER_BUDGET - currentPower;

            if (powerAvailable <= 0.1)
                break;

            int targetValue;
            // Balance thresholds for maximum efficiency
            double ratioThreshold = ratio[i];
            int userThreshold = userCount[i];
            
            if (userThreshold > MAX_USERS * 0.32 && ratioThreshold > 1.5)
            {
                targetValue = 100;
            }
            else if (userThreshold > MAX_USERS * 0.26 && ratioThreshold > 0.95)
            {
                targetValue = 94;
            }
            else if (userThreshold > MAX_USERS * 0.18 && ratioThreshold > 0.65)
            {
                targetValue = 78;
            }
            else if (userThreshold > MAX_USERS * 0.1 && ratioThreshold > 0.32)
            {
                targetValue = 62;
            }
            else if (ratioThreshold > 0.22)
            {
                targetValue = 42;
            }
            else if (ratioThreshold > 0.08)
            {
                targetValue = 25;
            }
            else
            {
                targetValue = 12;
            }

            if (targetValue > 100) targetValue = 100;

            // Check if targetValue fits in available power
            double basePower = (featurePower[i] * targetValue * targetValue) / 10000.0;
            double changePower = ABS(targetValue - prevFeatureValue[i]) * 0.01;
            double totalPowerNeeded = basePower + changePower;

            if (totalPowerNeeded > powerAvailable)
            {
                // Binary search for maximum feasible value
                int low = 0, high = targetValue;
                while (low < high)
                {
                    int mid = (low + high + 1) / 2;
                    double p = (featurePower[i] * mid * mid) / 10000.0;
                    double c = ABS(mid - prevFeatureValue[i]) * 0.01;
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

        // Try to use remaining power by incrementally increasing values
        for (int pass = 0; pass < 100; pass++)
        {
            double currentPower = 0;
            for (int f = 0; f < MAX_FEATURES; f++)
            {
                currentPower += (featurePower[f] * featureValue[f] * featureValue[f]) / 10000.0;
                currentPower += ABS(prevFeatureValue[f] - featureValue[f]) * 0.01;
            }

            double remainingPower = POWER_BUDGET - currentPower;
            if (remainingPower < 0.2)
                break;

            int improved = 0;
            
            // Try to increase best features first in early passes
            int passType = (pass < 40) ? 0 : 1;

            for (int idx = 0; idx < MAX_FEATURES; idx++)
            {
                if (passType == 1 && idx > 10)  // In later passes, focus on best features
                    break;
                    
                int i = sortedFeatures[idx];

                if (featureValue[i] >= 100)
                    continue;

                int newValue = featureValue[i] + 1;
                double oldBasePower = (featurePower[i] * featureValue[i] * featureValue[i]) / 10000.0;
                double newBasePower = (featurePower[i] * newValue * newValue) / 10000.0;
                double oldChangePower = ABS(prevFeatureValue[i] - featureValue[i]) * 0.01;
                double newChangePower = ABS(prevFeatureValue[i] - newValue) * 0.01;

                double extraPower = (newBasePower - oldBasePower) + (newChangePower - oldChangePower);

                if (currentPower + extraPower <= POWER_BUDGET)
                {
                    featureValue[i]++;
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