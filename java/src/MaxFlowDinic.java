import java.util.*;

public class MaxFlowDinic {

	static class Edge {
		int s, t, rev, cap, f;

		public Edge(int s, int t, int rev, int cap) {
			this.s = s;
			this.t = t;
			this.rev = rev;
			this.cap = cap;
		}
	}

	List<Edge>[] graph;
	int src, dest;
	int[] ptr, Q, dist;

	public void init(int nodes) {
		graph = new List[nodes];
		for (int i = 0; i < nodes; i++)
			graph[i] = new ArrayList<Edge>();
		ptr = new int[nodes];
		Q = new int[nodes];
		dist = new int[nodes];
	}

	public void addEdge(int s, int t, int cap) {
		graph[s].add(new Edge(s, t, graph[t].size(), cap));
		graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
	}

	boolean dinic_bfs() {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int sizeQ = 0;
		Q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (Edge e : graph[u]) {
				if (dist[e.t] < 0 && e.f < e.cap) {
					dist[e.t] = dist[u] + 1;
					Q[sizeQ++] = e.t;
				}
			}
		}
		return dist[dest] >= 0;
	}

	int dinic_dfs(int u, int f) {
		if (u == dest)
			return f;
		for (; ptr[u] < graph[u].size(); ++ptr[u]) {
			Edge e = graph[u].get(ptr[u]);
			if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
				int df = dinic_dfs(e.t, Math.min(f, e.cap - e.f));
				if (df > 0) {
					e.f += df;
					graph[e.t].get(e.rev).f -= df;
					return df;
				}
			}
		}
		return 0;
	}

	public int maxFlow(int src, int dest) {
		this.src = src;
		this.dest = dest;
		int flow = 0;
		while (dinic_bfs()) {
			Arrays.fill(ptr, 0);
			while (true) {
				int df = dinic_dfs(src, Integer.MAX_VALUE);
				if (df == 0)
					break;
				flow += df;
			}
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		MaxFlowDinic flow = new MaxFlowDinic();
		flow.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					flow.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == flow.maxFlow(0, 2));
	}
}
