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
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            featureValue[i] = 0;
        }

        // Precompute satisfaction contribution per unit for each feature
        double satPerUnit[MAX_FEATURES];
        
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            double total = 0;
            double qualityFactor = featureQuality[i] * 0.01;
            int median = featureMedian[i];
            
            for (int u = 0; u < MAX_USERS; u++)
            {
                int d = userDistance[u];
                if (d < MIN_DISTANCE) d = MIN_DISTANCE;
                if (d > MAX_DISTANCE) d = MAX_DISTANCE;
                int diff = (median - d);
                if (diff < 0) diff = -diff;
                total += qualityFactor / (diff + 1);
            }
            satPerUnit[i] = total;
        }

        // Calculate efficiency and sort
        struct FeatureInfo {
            int idx;
            double efficiency;
            double sat;
            int power;
        };
        
        struct FeatureInfo features[MAX_FEATURES];
        
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            features[i].idx = i;
            features[i].sat = satPerUnit[i];
            features[i].power = featurePower[i];
            // Use power^2 for efficiency since power cost is quadratic
            features[i].efficiency = satPerUnit[i] * 10000.0 / (featurePower[i] * featurePower[i]);
        }

        // Bubble sort by efficiency
        for (int i = 0; i < MAX_FEATURES - 1; i++)
        {
            for (int j = i + 1; j < MAX_FEATURES; j++)
            {
                if (features[i].efficiency < features[j].efficiency)
                {
                    struct FeatureInfo temp = features[i];
                    features[i] = features[j];
                    features[j] = temp;
                }
            }
        }

        // Allocate power based on efficiency rank
        for (int idx = 0; idx < MAX_FEATURES; idx++)
        {
            int i = features[idx].idx;
            int power = features[idx].power;
            
            // Calculate current power
            double currentPower = 0;
            for (int f = 0; f < MAX_FEATURES; f++)
            {
                int val = featureValue[f];
                currentPower += (featurePower[f] * val * val) / 10000.0;
                currentPower += ABS(prevFeatureValue[f] - val) * 0.01;
            }
            
            double powerAvail = POWER_BUDGET - currentPower;
            if (powerAvail < 0.5)
                break;

            // Target based on rank (more aggressive)
            int target;
            if (idx < 2) target = 100;
            else if (idx < 4) target = 95;
            else if (idx < 6) target = 85;
            else if (idx < 9) target = 70;
            else if (idx < 12) target = 55;
            else if (idx < 15) target = 40;
            else if (idx < 18) target = 25;
            else target = 12;

            // Binary search for max feasible value
            int low = 0, high = target;
            while (low < high)
            {
                int mid = (low + high + 1) / 2;
                double basePower = (power * mid * mid) / 10000.0;
                double changePower = ABS(mid - prevFeatureValue[i]) * 0.01;
                
                if (currentPower + basePower + changePower <= POWER_BUDGET)
                {
                    low = mid;
                }
                else
                {
                    high = mid - 1;
                }
            }
            featureValue[i] = low;
        }

        // Simple greedy fill for remaining power (limited iterations)
        for (int iter = 0; iter < 150; iter++)
        {
            double currentPower = 0;
            for (int f = 0; f < MAX_FEATURES; f++)
            {
                int val = featureValue[f];
                currentPower += (featurePower[f] * val * val) / 10000.0;
                currentPower += ABS(prevFeatureValue[f] - val) * 0.01;
            }
            
            double remaining = POWER_BUDGET - currentPower;
            if (remaining < 0.3)
                break;
            
            int best = -1;
            double bestGain = 0;
            
            for (int idx = 0; idx < MAX_FEATURES; idx++)
            {
                int i = features[idx].idx;
                if (featureValue[i] >= 100)
                    continue;
                
                int val = featureValue[i];
                int newVal = val + 1;
                int power = featurePower[i];
                
                double baseInc = (power * newVal * newVal - power * val * val) / 10000.0;
                double changeInc = ABS(newVal - prevFeatureValue[i]) * 0.01 - ABS(val - prevFeatureValue[i]) * 0.01;
                double powerInc = baseInc + changeInc;
                
                if (currentPower + powerInc > POWER_BUDGET)
                    continue;
                
                double netGain = features[idx].sat - powerInc * 0.01;
                
                if (netGain > bestGain)
                {
                    bestGain = netGain;
                    best = i;
                }
            }
            
            if (best == -1 || bestGain < 0.01)
                break;
            
            featureValue[best]++;
        }

        screen_control(featureValue);
        
        for (int i = 0; i < MAX_FEATURES; i++)
        {
            prevFeatureValue[i] = featureValue[i];
        }
    }
}