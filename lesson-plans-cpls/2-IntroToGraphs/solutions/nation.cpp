#include <bits/stdc++.h>

using namespace std;

int clique_size(vector<int> *graph, vector<int> &govs, int *visited, int starting) {
    visited[starting] = 1;
    int size = 1;

    for (int i = 0 ; i < graph[starting].size() ; i++) {
        if (!visited[graph[starting][i]]) {
            size += clique_size(graph, govs, visited, graph[starting][i]);
        }
    }

    return size;
}

int main(int argc, char *argv[]) {
    vector<int> graph[1001];
    vector<int> govs;
    int visited[1001] = {0};

    int n, m, k;

    cin >> n >> m >> k;

    for (int i = 0 ; i < k ; i++) {
        int c;
        cin >> c;
        govs.push_back(c);
    }

    for (int i = 0 ; i < m ; i++) {
        int u, v;
        cin >> u >> v;

        graph[u].push_back(v);
        graph[v].push_back(u);
    }

    int connected_points = 0;
    int max_state_size = 0;
    int answer = 0;

    for (int i = 0 ; i < k ; i++) {
        int count = clique_size(graph, govs, visited, govs[i]);
        connected_points += count;
        max_state_size = (count > max_state_size) ? count : max_state_size;
        answer += count * (count - 1) / 2;
    }

    // the remaining points should go to the largest clique
    int remaining = n - connected_points;
    answer -= max_state_size * (max_state_size - 1) / 2;
    max_state_size += remaining;
    answer += max_state_size * (max_state_size - 1) / 2;

    cout << answer - m << '\n';
    return 0;
}
