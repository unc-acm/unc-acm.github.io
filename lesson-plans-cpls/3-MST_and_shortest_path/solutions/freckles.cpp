#include <bits/stdc++.h>
#include <stdio.h>

using namespace std;

typedef pair<int, float> vertex_priority;

class vertex_comp {
    public:
        bool operator() (const vertex_priority &v1, const vertex_priority &v2) {
            return (v1.second > v2.second);
        };
};

typedef priority_queue<vertex_priority, vector<vertex_priority>, vertex_comp> vertex_cost_queue;

class Undirected_Graph {
    int V;
    vector<vector<float> > E;
  public:
    Undirected_Graph(int V) {
        this->V = V;

        for (int i = 0 ; i < V ; i++) {
            (this->E).push_back(vector<float>(i));
        }
    };

    Undirected_Graph(int V, vector<vector<float> > E) {
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
        bool visited[V] = {false};
        int visited_count = 0;

        costs.push({0, 0});

        while (!costs.empty()) {
            vertex_priority current = costs.top();
            costs.pop();

            if (visited[current.first]) {
                continue;
            }

            //cout << current.first << "(" << current.second << ")"<< '\n';

            for (int i = 0 ; i < current.first ; i++) {
                if (!visited[i] && E[i][current.first] > -0.5) {
                    //cout << "Pushed edge from " << current.first;
                    //cout << " to " << i << " (" << E[i][current.first] << ")" << '\n';
                    costs.push({i, E[i][current.first]});
                }
            }

            for (int i =  current.first + 1 ; i < V ; i++) {
                if (!visited[i] && E[current.first][i] > -0.5) {
                    //cout << "Pushed edge from " << current.first;
                    //cout << " to " << i << " (" << E[current.first][i] << ")" << '\n';
                    costs.push({i, E[current.first][i]});
                }
            }

            total_cost += current.second;

            visited[current.first] = true;
            visited_count++;
        }

        if (visited_count == this->V) {
            return total_cost;
        }

        else {
            return -1;
        }
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
            for (int j = i + 1 ; j < V ; j++) {
                G.add_edge(i, j, dist(pts, i, j));
            }
        }

        cout << G.MST() << "\n\n";
    }

    return 0;
}

