#include <iostream>
#include <queue>
#include <vector>
#include <utility>

using namespace std;

int solve(int N, bool **E) {
    bool visited[N];
    int degrees[N];
    bool branches = false;
    bool pushed = false;
    queue<int> q;
    
    for (int i = 0 ; i < N ; i++) {
        visited[i] = false;
        degrees[i] = 0;

        for (int j = 0 ; j < N ; j++) {
            if (E[j][i]) {
                degrees[i]++;
            }
        }

        if (degrees[i] == 0) {
            if (!pushed) {
                pushed = true;
            }
            else {
                branches = true;
            }
            q.push(i);
        }
    }

    if (!pushed) { // one big cycle
        return 0;
    }

    while (!q.empty()) {
        pushed = false;
        int a = q.front();
        q.pop();
        visited[a] = true;

        for (int i = 0 ; i < N ; i++) {
            if (E[a][i]) {
                if (--degrees[i] == 0) {
                    if (!pushed) {
                        pushed = true;
                    }
                    else {
                        branches = true;
                    }
                    q.push(i);
                }
            }
        }
    }

    for (int i = 0 ; i < N ; i++) {
        if (!visited[i]) {
            return 0; // if there sort terminated prematurely, there is a cycle
        }
    }

    if (branches) {
        return 2;
    }

    else {
        return 1;
    }
}

int main(int argc, char *argv[]) {
    int N, M, to, from;

    cin >> N >> M;

    while (N || M) {
        bool **E = new bool*[N];
        for (int i = 0 ; i < N ; i++) {
            E[i] = new bool[N];
            for (int j = 0 ; j < N ; j++) {
                E[i][j] = false;
            }
        }

        for (int i = 0 ; i < M ; i++) {
            cin >> from >> to;
            E[from-1][to-1] = true;
        }

        cout << solve(N, E) << '\n';

        cin >> N >> M;
    }
    return 0;
}
