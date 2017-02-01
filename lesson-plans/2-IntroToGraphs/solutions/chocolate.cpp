#include <bits/stdc++.h>

using namespace std;

typedef struct step {
    long long minute;
    long long a, b;
} step;

map<long long, step> ASteps;
map<long long, step> BSteps;

queue<step> AQueue;
queue<step> BQueue;

int main(int argc, char *argv[]) {
    long long a1, b1, a2, b2;
    cin >> a1 >> b1 >> a2 >> b2;

    AQueue.push({0, a1, b1});
    BQueue.push({0, a2, b2});

    while (!AQueue.empty() || !BQueue.empty()) {
        // meet in the middle bfs
        if (!AQueue.empty()) {
            step ACurrent = AQueue.front();
            AQueue.pop();
            long long ASize = ACurrent.a * ACurrent.b;
            if (!ASteps.count(ASize)) {
                if (BSteps.count(ASize)) {
                    step BPair = BSteps[ASize];
                    cout << ACurrent.minute + BPair.minute << '\n';
                    cout << ACurrent.a << ' ' << ACurrent.b << '\n';
                    cout << BPair.a << ' ' << BPair.b << '\n';
                    return 0;
                }

                if (ACurrent.a % 2 == 0) {
                    AQueue.push({ACurrent.minute+1, ACurrent.a / 2, ACurrent.b});
                }

                if (ACurrent.a % 3 == 0) {
                    AQueue.push({ACurrent.minute+1, ACurrent.a * 2 / 3, ACurrent.b});
                }

                if (ACurrent.b % 2 == 0) {
                    AQueue.push({ACurrent.minute+1, ACurrent.a, ACurrent.b / 2});
                }

                if (ACurrent.b % 3 == 0) {
                    AQueue.push({ACurrent.minute+1, ACurrent.a, ACurrent.b * 2 / 3});
                }

                ASteps[ASize] = ACurrent;
            }
        }

        if (!BQueue.empty()) {
            step BCurrent = BQueue.front();
            BQueue.pop();
            long long BSize = BCurrent.a * BCurrent.b;
            if (!BSteps.count(BSize)) {
                if (ASteps.count(BSize)) {
                    step APair = ASteps[BSize];
                    cout << BCurrent.minute + APair.minute << '\n';
                    cout << APair.a << ' ' << APair.b << '\n';
                    cout << BCurrent.a << ' ' << BCurrent.b << '\n';
                    return 0;
                }

                if (BCurrent.a % 2 == 0) {
                    BQueue.push({BCurrent.minute+1, BCurrent.a / 2, BCurrent.b});
                }

                if (BCurrent.a % 3 == 0) {
                    BQueue.push({BCurrent.minute+1, BCurrent.a * 2 / 3, BCurrent.b});
                }

                if (BCurrent.b % 2 == 0) {
                    BQueue.push({BCurrent.minute+1, BCurrent.a, BCurrent.b / 2});
                }

                if (BCurrent.b % 3 == 0) {
                    BQueue.push({BCurrent.minute+1, BCurrent.a, BCurrent.b * 2 / 3});
                }

                BSteps[BSize] = BCurrent;
            }
        }
    }

    cout << "-1\n";

    return 0;
}
