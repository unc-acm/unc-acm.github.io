#include <bits/stdc++.h>
#include <stdio.h>

using namespace std;

typedef pair<int, float> vertex_priority;

class vertex_comp {
    public:
        bool operator() (const vertex_priority &v1, const vertex_priority &v2) {
            if (v1.second - v2.second > 0.00001) {
                return 1;
            }
            else if (v1.second - v2.second > -0.00001) {
                return 0;
            }
            else {
                return -1;
            }
        };
};

typedef priority_queue<vertex_priority, vector<vertex_priority>, vertex_comp> vertex_cost_queue;

class Undirected_Graph {
    int V;
    float **E;
    public:
    Undirected_Graph(int V) {
        this->V = V;

        this->E = new float*[V];

        for (int i = 0 ; i < V ; i++) {
            (this->E)[i] = new float[i]();
        }
    };

    Undirected_Graph(int V, float **E) {
        this->V = V;
        this->E = E;
    };
    void add_edge(int from, int to, float len) {
        if (from > to) {
            add_edge(to, from, len);
        }
        else {
            E[from][to] = len;
        }
    };
    float MST() {
        float total_cost = 0;
        vertex_cost_queue costs;
        vertex_priority current;
        set<int> S;
        int prev_node[V];
        bool visited[V];

        // pick V[0] as our starting pt
        visited[0] = true;
        for (int i = 1 ; i < V ; i++) {
            if (E[i][0]) {
                costs.push(make_pair(i, E[i][0]));
            }
            else {
                costs.push(make_pair(i, INT_MAX));
            }
            S.insert(i);
            prev_node[i] = 0;
            visited[i] = false;
        }

        while (!S.empty()) {
            current = costs.top();
            costs.pop();
            S.erase(current.first);

            visited[current.first] = true;

            if (current.first < prev_node[current.first]) {
                total_cost += E[current.first][prev_node[current.first]];
            }
            else {
                total_cost += E[prev_node[current.first]][current.first];

            }

            for (int i = 0 ; i < V ; i++) {
                if (!visited[i]) {
                    if (current.first > i) {
                        if (prev_node[i] > i) {
                            if (E[i][current.first] < E[i][prev_node[i]]) {
                                costs.push(make_pair(i, E[i][current.first]));
                                prev_node[i] = current.first;
                            }
                        }
                        else {
                            if (E[i][current.first] < E[prev_node[i]][i]) {
                                costs.push(make_pair(i, E[i][current.first]));
                                prev_node[i] = current.first;
                            }
                        }
                    }
                    else {
                        if (prev_node[i] > i) {
                            if (E[current.first][i] < E[i][prev_node[i]]) {
                                costs.push(make_pair(i, E[current.first][i]));
                                prev_node[i] = current.first;
                            }
                        }
                        else {
                            if (E[current.first][i] < E[prev_node[i]][i]) {
                                costs.push(make_pair(i, E[current.first][i]));
                                prev_node[i] = current.first;
                            }
                        }
                    }
                }
            }
        }

        return total_cost;
    };
};

float dist(vector<pair<float, float> > pts, int i, int j) {
    return sqrt(pow((pts[i].first - pts[j].first), 2) + pow((pts[i].second - pts[j].second), 2));
}


int main(int argc, char *argv[]) {
    int N;
    cin >> N;
    cout << fixed;
    cout << setprecision(2);

    for (int a = 0 ; a < N ; a++) {
        int V;
        cin >> V;
        Undirected_Graph G(V);

        vector<pair<float, float> > pts(V);

        for (int i = 0 ; i < V ; i++) {
            cin >> pts[i].first >> pts[i].second;
        }

        for (int i = 0 ; i < V ; i++) {
            for (int j = i ; j < V ; j++) {
                G.add_edge(i, j, dist(pts, i, j));
            }
        }

        cout << G.MST() << "\n\n";
    }
    return 0;
}

